package socket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import po.Order;
import service.CommodityService;
import vo.Instruction;
import vo.ItemVO;

@ServerEndpoint("/websocket/{clientname}")
public class WebSocket {

	// 存储当前所有连接的客户端
	private static Map<String, WebSocket> clients = new ConcurrentHashMap<>();
	private static Order order = null;

	private Session session = null;
	private String clientname = null;
	private CommodityService service = new CommodityService();
	private Gson gson = new Gson();

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(@PathParam("clientname") String clientname, Session session) {
		if (clientname.equals("phone")) {
			Instruction instruction = new Instruction();
			if(clients.get("phone") != null) {// 新手机连接但当前机器被占用
				System.out.println("手机连接！售货机当前被占用！");
				instruction.setOperation("occupied");
			} else if (clients.get("machine") == null) { //手机连接但售货机未连接
				System.out.println("售货机离线！");
				instruction.setOperation("offline");
			}
			try {
				session.getBasicRemote().sendText(gson.toJson(instruction));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.clientname = clientname;
		this.session = session;
		System.out.println(clientname + "加入了连接!");
		clients.put(this.clientname, this);
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		if (this.clientname != null) {
			clients.remove(this.clientname);
			System.out.println(this.clientname + "退出了连接!");
		}
	}

	/**
	 * 收到客户端消息后调用的方法 根据instruction对象中的operation字段的数据决定调用何种方法，作何种处理
	 * 
	 * @param message 客户端发送过来的消息 Json化的instruction类对象
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		Instruction instruction = gson.fromJson(message, Instruction.class);
		System.out.println(this.clientname + "发来消息:" + instruction);
		String operation = instruction.getOperation();
		if (operation.equals("open")) { // 网页发送来“open”的命令，转发给售货机开门
			openMachine(message);
		} else if (operation.equals("add")) { // 售货机发送来添加命令
			addToCart(instruction);
		} else if (operation.equals("del")) { // 售货机发送来删除命令
			delFromCart(message);
		} else if (operation.equals("settleup")) { // 售货机关门，通知前端结算
			settleUp(message);
		} else if (operation.equals("checkout")) {
			checkOut(message);
		}
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}

	public void sendMessage(String dst, String message) {
		WebSocket socket = clients.get(dst);
		if (socket != null) {
			try {
				socket.session.getBasicRemote().sendText(message);
				System.out.println("发送给:" + dst + " 消息:" + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通知售货机开门
	 */
	public void openMachine(String instruction) {
		order = new Order();
		sendMessage("machine", instruction);
	}

	/**
	 * 售货机检测到添加，通知前端添加
	 * 
	 * @param itemStr 售货机发来的包含添加id的字符串
	 */
	public void addToCart(Instruction instruction) {
		int id = instruction.getItem().getId();
		ItemVO item = service.findById(id);
		order.addItem(item);
		instruction.setItem(item);
		sendMessage("phone", gson.toJson(instruction));
	}

	/**
	 * 售货机检测到删除，通知前端进行删除
	 * 
	 * @param instrcution instruction json
	 */
	public void delFromCart(String instrcution) {
		order.addItem(gson.fromJson(instrcution, Instruction.class).getItem());
		sendMessage("phone", instrcution);
	}

	/**
	 * 售货机关门，通知前端结算
	 */
	public void settleUp(String instruction) {
		sendMessage("phone", instruction);
	}

	/**
	 * 前端发来订单，服务器端结算
	 * 
	 * @param message
	 */
	public void checkOut(String message) {
		String orderJson = message.substring(message.indexOf("\"item\":") + 7, message.lastIndexOf("}"));
		Order order = gson.fromJson(orderJson, Order.class);
		System.out.println(order);
	}
}
