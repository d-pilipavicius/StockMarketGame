import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class gameSetup extends PApplet {

int win = 0;
menu MenuBar;
stock[] Stock;
int buyWin = -1; //<>//
float myBalance = 80000;
float[][] lastStock;
int n = 1;
boolean taken = false;

public void setup()
{
   //(1920,1080)
  frameRate(60);
  textAlign(CENTER,CENTER);
  Stock = new stock[6];
  lastStock = new float[6][80];
  MenuBar = new menu(width*9/10,0);
  writeStock();
}

public void draw()
{
  if(win !=4)
  {
    switch(win)
    {
      case 0:
        stockGraph();
        toLines();
        break;
      case 1:
        shop(); //<>//
        break;
      case 2: //Exit app.
        exitGame();
        break;
      case 3:
        modBoard();
        break;
      case 97:
        slider();
        break;
      case 98:
        buyPad();
        break;
      case 99:
        shopWindow();
        break;
      case 100:
        errorBox(errorMessage);
        break;
    }
    if(win != 97 && win != 98 && win != 99 && win != 100) crMenu();
    if(win != 2) newValue(); //Exit window included.
    if(!taken)
    {
      x1 = PApplet.parseInt(map(time,1,3600,width*17/64,width*47/64));
      taken = true;
    }
  }
  else if(win == 4)
  {
    background(0);
    fill(255);
    textSize(150);
    text("PAUSED",width/2,height/2);
    textSize(30);
    text("Press anywhere to unpause", width/2, height*3/4);
    if(mousePressed)
    {
      mousePressed = false;
      win = lastMenu;
    }
  }
}
int time = 300;
int x1;
public void newValue()
{
  if(frameCount%time == 0) 
  {
    if (n!=80)n++;
    for(int i = 0; i<6; i++)
    {
      if(Stock[i].value>50000) Stock[i].value*=random(0.9f,1.1f);
      else Stock[i].value*=random(0.9f,1.4f);
      if(Stock[i].value<10) Stock[i].value=10;
      else if(Stock[i].value>1000000) Stock[i].value=1000000;
      for(int j = 0; j<n; j++)
      {
        if(j==n-1)
        {
          lastStock[i][0] = Stock[i].value;
          break;
        }
        lastStock[i][n-j-1] = lastStock[i][n-j-2];
      }
    }
  }
}

public void toLines()
{
  for(int i = 0; i<6; i++)
  {
    if(!Stock[i].checked) continue;
    for(int j = 0; j<n; j++)
    {
      strokeWeight(1);
      stroke(Stock[i].graph);
      if(n-1-j!=0) line(j*width*3/320+width/8,height*3/4+height/8-map(lastStock[i][n-j-1],0,1000000,0,height*3/4),(j+1)*width*3/320+width/8,height*3/4+height/8-map(lastStock[i][n-j-2],0,1000000,0,height*3/4));
    }
  }
}
boolean stopApp = false;
int lastMenu = 0;
class menu
{
  int mainMenu = color(150,150,150);
  int x, y;
  menu(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public void createMain()
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
  public void createMenu(int i, String txt)
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
//win = 0 is stock graph.
//win = 1 is stock shop.
//win = 99 is buy window.
class stock
{
  boolean checked = true;
  boolean buy = false;
  int bought = 0;
  String name;
  float value;
  int graph;
  stock(String name, float value, int graph)
  {
    this.name = name;
    this.value = value;
    this.graph = graph;
  }
  public void name(int x, int y)
  {
    fill(0);
    textSize(50);
    text(name,x,y);
    textSize(20);
    text("Price per unit: "+nf(value,0,2)+"$",x,y+width*5/108);
  }
  public void button(int x, int y)
  {
    x-=width/50;
    y-=height/50;
    fill(0,255,0);
    rect(x,y,width/25,height/25);
    fill(0);
    textSize(15);
    text("Buy",x+width/50,y+height/50);
    if(mousePressed && mouseX>x && mouseX<x+width/25 && mouseY>y && mouseY<y+height/25 && !buying) 
    {
      buy = true;
      lastMenu = win;
      win = 99;
      mousePressed = false;
    }
  }
  public void check(int x, int y)
  {
    strokeWeight(1);
    stroke(graph);
    fill(graph);
    rect(x-width/96,y,width/500,width/500);
    stroke(255);
    fill(0);
    strokeWeight(3);
    rect(x-width/192,y-width/192,width/96,width/96);
    fill(255);
    text(name,x,y+width/48);
    text("Value of stock: "+nf(value,0,2)+"$",x+width/23,y);
    if(checked)
    {
      stroke(0,255,0);
      line(x-width/192,y-width/192,x+width/192,y+width/192);
      line(x+width/192,y-width/192,x-width/192,y+width/192);
    }
    if(x-width/192<mouseX && x+width/192>mouseX &&
    y-width/192<mouseY && y+width/192>mouseY && mousePressed)
    {
      mousePressed = false;
      checked = !checked;
    }
  }
}
public void writeStock()
{
  Stock[0] = new stock("Prienai",random(1000,1000000),color(255,0,255));
  Stock[1] = new stock("Kauno marozai inc.",random(1000,1000000),color(77,57,186));
  Stock[2] = new stock("Pica su ananasais",random(1000,1000000),color(0,255,0));
  Stock[3] = new stock("Tešla",random(1000,1000000),color(255,0,0));
  Stock[4] = new stock("Nausėdos troba",random(1000,1000000),color(0,255,255));
  Stock[5] = new stock("Kleckoinai",random(1000,1000000),color(255,255,0));
  for(int i = 0; i<6; i++)
  {
    lastStock[i][0]=Stock[i].value;
  }
}
int potent = 0; //Amount of stocks planning to purchase;
int errorMessage = -1;

boolean clickMenu = false;
boolean buying = false;

public void crMenu()
{
  MenuBar.createMain();
  if(clickMenu)
  {
    MenuBar.createMenu(1,"Graph");
    MenuBar.createMenu(2,"Shop");
    MenuBar.createMenu(3,"Exit game");
    MenuBar.createMenu(4,"Mods");
    MenuBar.createMenu(5,"Pause");
  }
}

public void exitGame()
{
  fill(200);
  rect(width/2-width/10,height/2-height/10,width/5,height/5);
  fill(0);
  textSize(30);
  text("Are you sure you\nwant to exit?",width/2,height*9/20);
  fill(0,255,0);
  rect(width/2-width/50-width/20,height/2+height/50,width/25,height/25);
  fill(255,0,0);
  rect(width/2-width/50+width/20,height/2+height/50,width/25,height/25);
  fill(0);
  textSize(15);
  text("Exit",width/2-width/20,height/2+height/25);
  text("Cancel",width/2+width/20,height/2+height/25);
  if(mouseX>width/2-width/50-width/20 && 
  mouseX<width/2-width/50-width/20+width/25 && 
  mouseY>height/2+height/50 && 
  mouseY<height/2+height/50+height/25 && mousePressed)
  {
    exit();
  }
  else if(mouseX>width/2-width/50+width/20 && 
  mouseX<width/2-width/50+width/20+width/25 && 
  mouseY>height/2+height/50 && 
  mouseY<height/2+height/50+height/25 && mousePressed)
  {
    mousePressed = false;
    win = lastMenu;
  }
}

public void stockGraph()
{
  background(0);
  stroke(255);
  strokeWeight(1);
  line(width/8,height/8,width/8,height/8*7);
  line(width/8,height/8*7,width/8*7,height/8*7);
  strokeWeight(0.5f);
  for(int i = 0; i<10; i++)
  {
    line(width/8,height/8+height*3*i/40,width/8*7,height/8+height*3*i/40);
  }
  for(int i = 1; i<=80; i++)
  {
    line(width/8+width*3/320*i,height*6.95f/8,width/8+width*3/320*i,height*7/8);
    fill(255);
    textSize(10);
    text(i+"",width/8+width*3/320*i,height*7/8+height/108);
  }
  for(int i = 0; i<6; i++)
  {
    Stock[i].check(width/30,height/6+height/15*i);
  }
}

public void shop()
{
  background(200);
  fill(0);
  textSize(50);
  text(nf(myBalance,0,2)+" $",width/2,height/20);
  fill(255);
  rect(width/8,height/8,width*3/4,height*3/4);
  for(int i = 1; i<=2; i++)
  {
    stroke(0);
    strokeWeight(1);
    line(width/8,height/8+height*3*i/12,width/8*7,height/8+height*3*i/12);
  }
  line(width/2,height/8,width/2,height*7/8);
  for(int i = 0; i<2; i++)
  {
    for(int j = 0; j<3; j++)
    {
      Stock[i*3+j].button(width*5/16+width*3/8*i,height*13/40+height*3/12*j);
      Stock[i*3+j].name(width*5/16+width*3/8*i,height/5+height*3/12*j);
      if(Stock[i*3+j].buy) buyWin = i*3+j;
    }
  }
  
}

public void shopWindow()
{
  if(!buying)
  {
    background(200);
    fill(0);
    textSize(50);
    text(nf(myBalance,0,2)+" $",width/2,height/20);
    fill(255);
    rect(width/2-width/10,height/2-height/10,width/5,height/5);
    fill(0);
    textSize(30);
    text(Stock[buyWin].name,width/2,height*9/20);
    textSize(15);
    text("You owe: "+Stock[buyWin].bought,width/2,height*9/20+height/20);
    fill(0,255,0);
    rect(width/2-width/50-width/20,height/2+height/50,width/25,height/25);
    fill(255,0,0);
    rect(width/2-width/50+width/20,height/2+height/50,width/25,height/25);
    fill(0);
    textSize(15);
    text("Buy/Sell",width/2-width/20,height/2+height/25);
    text("Cancel",width/2+width/20,height/2+height/25);
  }
  else if(buying)
  {
    win = 98;
  }
  if(mouseX>width/2-width/50-width/20 && 
  mouseX<width/2-width/50-width/20+width/25 && 
  mouseY>height/2+height/50 && 
  mouseY<height/2+height/50+height/25 && mousePressed)
  {
    buying = true;
    mousePressed = false;
  }
  else if(mouseX>width/2-width/50+width/20 && 
  mouseX<width/2-width/50+width/20+width/25 && 
  mouseY>height/2+height/50 && 
  mouseY<height/2+height/50+height/25 && mousePressed && !buying)
  {
    mousePressed = false;
    win = 1;
    Stock[buyWin].buy = false;
    buyWin = -1;
  }
}

public void buyPad()
{
  background(200);
  fill(0);
  textSize(50);
  text(nf(myBalance,0,2)+" $",width/2,height/20);
  strokeWeight(1);
  stroke(0);
  fill(150);
  rect(width*57/128,height*71/216,width*7/64,height*10/27); //The pad itself.
  fill(255);
  rect(width*175/384,height*3/8,width*17/192,height/27); //Amout bar.
  rect(width*67/128,height*3/8,width/48,height/27); //Clear button.
  fill(0);
  textSize(20);
  text(potent,width*47/96,height*85/216);
  textSize(15);
  text("Price: "+nf(Stock[buyWin].value,0,2)+"$",width/2,height*19/54);
  text("CLR",width*205/384,height*85/216);
  if(mouseX>width*67/128 && mouseX<width*209/384 && mouseY>height*3/8 && mouseY<height*89/216 && mousePressed)
  {
    mousePressed = false;
    potent = 0;
  }
  for(int i = 0; i<3; i++)
  {
    for(int j = 0; j<4; j++)
    {
      if(i == 0 && j == 3) fill(0,255,0);
      else if(i == 2 && j == 3) fill(255,0,0);
      else fill(255);
      rect(width*175/384+width/32*i,height*91/216+height/18*j,width*5/192,height*5/108);
      fill(0);
      if(j != 3) text(i+j*3+1,width*15/32+width/32*i,height*4/9+height/18*j);
      else if(j == 3)
      {
        switch(i)
        {
          case 0:
            text("Buy",width*15/32+width/32*i,height*4/9+height/18*j);
            break;
          case 1:
            text(0,width*15/32+width/32*i,height*4/9+height/18*j);
            break;
          case 2:
            text("Sell",width*15/32+width/32*i,height*4/9+height/18*j);
            break;
        }
      }
      if(j==3 && i == 2)
      {
        fill(255);
        fill(120,255,120);
        rect(width*175/384,height*139/216,width/24,height*5/108);
        fill(255,120,120);
        rect(width*193/384,height*139/216,width/24,height*5/108);
        fill(0);
        text("Max buy",width*61/128,height*2/3);
        text("Max sell",width*67/128,height*2/3);
      }
      if(mousePressed)
      {
        mousePressed = false;
        if(mouseY>height*91/216 && mouseY<height*101/216)
        {
          if(mouseX>width*175/384 && mouseX<width*185/384 && potent<=9000000)
          {
            potent = potent*10+1;
          }
          else if(mouseX>width*187/384 && mouseX<width*197/384 && potent<=9000000)
          {
            potent = potent*10+2;
          }
          else if(mouseX>width*199/384 && mouseX<width*209/384 && potent<=9000000)
          {
            potent = potent*10+3;
          }
        }
        else if(mouseY>height*103/216 && mouseY<height*113/216)
        {
          if(mouseX>width*175/384 && mouseX<width*185/384 && potent<=9000000)
          {
            potent = potent*10+4;
          }
          else if(mouseX>width*187/384 && mouseX<width*197/384 && potent<=9000000)
          {
            potent = potent*10+5;
          }
          else if(mouseX>width*199/384 && mouseX<width*209/384 && potent<=9000000)
          {
            potent = potent*10+6;
          }
        }
        else if(mouseY>height*115/216 && mouseY<height*125/216)
        {
          if(mouseX>width*175/384 && mouseX<width*185/384 && potent<=9000000)
          {
            potent = potent*10+7;
          }
          else if(mouseX>width*187/384 && mouseX<width*197/384 && potent<=9000000)
          {
            potent = potent*10+8;
          }
          else if(mouseX>width*199/384 && mouseX<width*209/384 && potent<=9000000)
          {
            potent = potent*10+9;
          }
        }
        else if(mouseY>height*127/216 && mouseY<height*137/216)
        {
          if(mouseX>width*175/384 && mouseX<width*185/384)
          {
            if(Stock[buyWin].value*potent>myBalance)
            {
              win = 100;
              errorMessage = 0;
              Stock[buyWin].buy = false;
              buyWin = -1;
              potent = 0;
            }
            else
            {
              win = lastMenu;
              buying = false;
              myBalance -= Stock[buyWin].value*potent;
              Stock[buyWin].bought += potent;
              potent = 0;
              Stock[buyWin].buy = false;
              buyWin = -1;
            }
          }
          else if(mouseX>width*187/384 && mouseX<width*197/384 && potent<=9000000)
          {
            potent = potent*10;
          }
          else if(mouseX>width*199/384 && mouseX<width*209/384)
          {
            if(potent>Stock[buyWin].bought)
            {
              win = 100;
              errorMessage = 1;
              Stock[buyWin].buy = false;
              buyWin = -1;
              potent = 0;
            }
            else
            {
              win = lastMenu;
              buying = false;
              myBalance += potent*Stock[buyWin].value;
              Stock[buyWin].bought -= potent;
              potent = 0;
              Stock[buyWin].buy = false;
              buyWin = -1;
            }
          }
        }
        else if(mouseY>height*139/216 && mouseY<height*149/216)
        {
          if(mouseX>width*175/384 && mouseX<width*191/384)
          {
            potent = floor(myBalance/Stock[buyWin].value);
          }
          else if(mouseX>width*193/384 && mouseX<width*209/384)
          {
            potent = Stock[buyWin].bought;
          }
        }
      }
    }
  }
}

public void errorBox(int whatError)
{
  background(200);
  fill(255);
  rect(width/2-width/10,height/2-height/10,width/5,height/5);
  fill(0);
  textSize(30);
  switch(whatError)
  {
    case -1:
      text("null",width/2,height*9/20);
      break;
    case 0:
      text("Insufficient funds",width/2,height*9/20);
      break;
    case 1:
      text("Insufficient stock",width/2,height*9/20);
      break;
  }
  fill(0,255,0);
  rect(width*12/25,height*27/50,width/25,height/25);
  fill(0);
  textSize(15);
  text("OK",width/2,height*14/25);
  if(mouseX>width*12/25 && mouseX<width*13/25 && mouseY>height*27/50 && mouseY<height*29/50 && mousePressed)
  {
    buying = false;
    mousePressed = false;
    whatError = -1;
    win = lastMenu;
  }
}

public void modBoard()
{
  background(200);
  textSize(50);
  fill(random(0,255),random(0,255),random(0,255));
  text("Mod menu",width/2,height/8);
  for(int i = 0; i<1; i++)
  {
    for(int j = 0; j<1; j++)
    {
      fill(255);
      rect(width/4+width/4*i,height/4+height/4*j,width/4,height/4);
      fill(200,255,200);
      rect(width*71/200+width/4*i,height*54/125+height/4*j,width/25,height/25);
      fill(0);
      textSize(15);
      text("Change",width*3/8+width/4*i,height*113/250+height/4*j);
    }
  }
  textSize(30);
  text("Change how fast do the\nstock prices update",width*3/8,height*5/16);
  if(mouseX>width*71/200 && mouseX<width*79/200 && mouseY>height*54/125 && mouseY<height*59/125 && mousePressed)
  {
    mousePressed = false;
    win = 97;
  }
}

public void slider()
{
  background(200);
  fill(255);
  rect(width/4,height*2/5,width/2,height/5);
  fill(150);
  rect(width*17/64,height*13/25,width*15/32,height/108);
  fill(0);
  textSize(15);
  text(nf(PApplet.parseFloat(1)/60,0,2)+"s",width*17/64,height*493/900);
  text(60+"s",width*47/64,height*493/900);
  rect(x1,height*2783/5400,width/192,height/54);
  fill(0,255,0);
  rect(width*43/96,height*11/20,width*5/48,height/25);
  textSize(20);
  fill(0);
  text("Set the value",width/2,height*57/100);
  if(mouseX>width*17/64 && mouseX<width*47/64 && mouseY>height*2783/5400 && mouseY<height*961/1800 && mousePressed)
  {
    x1 = mouseX-width/384;
  }
  else if(mouseX>width*43/96 && mouseX<width*53/96 && mouseY>height*11/20 && mouseY<height*59/100 && mousePressed)
  {
    mousePressed = false;
    time = PApplet.parseInt(map(x1,width*101/384,width*47/64,1,3600));
    taken = false;
    win = 3;
  }
  else mousePressed = false;
  fill(0);
  textSize(30);
  text(nf(map(x1,width*101/384,width*47/64,1,3600)/60,0,2)+"s",width/2,height*11/25);
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "gameSetup" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
