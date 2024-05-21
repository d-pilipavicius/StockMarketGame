int win = 0;
menu MenuBar;
stock[] Stock;
int buyWin = -1; //<>//
float myBalance = 80000;
float[][] lastStock;
int n = 1;
boolean taken = false;

void setup()
{
  fullScreen(); //(1920,1080)
  frameRate(60);
  textAlign(CENTER,CENTER);
  Stock = new stock[6];
  lastStock = new float[6][80];
  MenuBar = new menu(width*9/10,0);
  writeStock();
}

void draw()
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
      x1 = int(map(time,1,3600,width*17/64,width*47/64));
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
