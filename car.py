import websocket
import _thread
import time
import  cv2


def on_message(ws, message):
    print(message)


def on_error(ws, error):
    print(error)


def on_close(ws, close_status_code, close_msg):
    print("### closed ###")


def on_open(ws):
    def run(*args):
        adress = "http://donkeycar:8887/video"
        cap = cv2.VideoCapture(adress)
        ret, frame = cap.read()
        # height, width, number of channels in image
        height = frame.shape[0]
        width = frame.shape[1]

        while True:
            ret, frame = cap.read()
            #write that to disk
            #cv2.imwrite('screenshot.png',frame)
            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            frame = frame[(int)(height/2):height, 0:width]
            frame = cv2.GaussianBlur(frame, (5,5), 1.5)
            _, max_val, _, max_loc= cv2.minMaxLoc(frame)
            ws.send(f"{{\"angle\":{(max_loc[0] - (width / 2)) / (width / 2) * 2},\"throttle\":{0.20},\"drive_mode\":\"user\",\"recording\":false}}")
            print(f"{{\"angle\":{(max_loc[0] - (width / 2)) / (width / 2) * 2},\"throttle\":{0.20},\"drive_mode\":\"user\",\"recording\":false}}")
  
    _thread.start_new_thread(run, ())


if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp("ws://donkeycar:8887/wsDrive",
                              on_open=on_open,
                              on_message=on_message,
                              on_error=on_error,
                              on_close=on_close)

    ws.run_forever()
