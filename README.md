# AppendCopy
复制拼接

 <figure class="third">
     <img src="screen_1.jpg">
     <img src="screen_2.jpg">
     <img src="screen_3.jpg">
 </figure>
 
 <table>
     <tr>
         <td ><center><img src="screen_1" >ScreenShut_1</center></td>
       <td ><center><img src="screen_2" >ScreenShut_2</center></td>
            <td ><center><img src="screen_3" >ScreenShut_3</center></td> 
      </tr>
 </table>
  
一个实现 非连续复制 的小工具。

本应用没有界面，打开后会自动转到后台，进入监听模式。
发生复制事件时，会在屏幕右侧弹出一个三秒自动消失的灰色悬浮球。点击悬浮球，球会变绿进入工作模式。此后悬浮球会对复制操作进行计数，每次进行复制操作后，后台自动收集复制的文本并清空剪贴板。
复制了所有内容后，点击绿色图标图标返回监听模式，软件在后台将拼接好的文本写入剪贴板。

软件会自动判断剪贴板复制的类别是否与上一次相同；如果相同，会自动拼接两次复制的内容。如果不同，直接拼接两次复制的内容并在中间增加换行。