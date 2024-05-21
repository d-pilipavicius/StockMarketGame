class stock
{
  boolean checked = true;
  boolean buy = false;
  int bought = 0;
  String name;
  float value;
  color graph;
  stock(String name, float value, color graph)
  {
    this.name = name;
    this.value = value;
    this.graph = graph;
  }
  void name(int x, int y)
  {
    fill(0);
    textSize(50);
    text(name,x,y);
    textSize(20);
    text("Price per unit: "+nf(value,0,2)+"$",x,y+width*5/108);
  }
  void button(int x, int y)
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
  void check(int x, int y)
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
