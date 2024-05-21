boolean stopApp = false;
int lastMenu = 0;
class menu
{
  color mainMenu = color(150,150,150);
  int x, y;
  menu(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  void createMain()
  {
    strokeWeight(1);
    stroke(0);
    fill(mainMenu);
    rect(x,y,width-x,height/25);
    textSize(25);
    fill(0);
    text("Menus",(x+width)/2,(y+height/25)/2);
    if(mouseX > x && mouseX < width && mouseY > y && mouseY < y + height/25 && mousePressed)
    {
      mousePressed = false;
      clickMenu = !clickMenu;
    }
  }
  void createMenu(int i, String txt)
  {
    strokeWeight(1);
    stroke(0);
    fill(230);
    rect(x,y+height/25*i,width-x,height/25);
    textSize(25);
    fill(0);
    text(txt,(x+width)/2,(y+height/25)/2+height/25*i);
    if(mouseX > x && mouseX < width && mouseY > y+height/25*i && mouseY < y+height/25*i+height/25 && mousePressed)
    {
      lastMenu = win;
      win = i-1;
      mousePressed = false;
    }
  }
}
