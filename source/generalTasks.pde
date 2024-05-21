int time = 300;
int x1;
void newValue()
{
  if(frameCount%time == 0) 
  {
    if (n!=80)n++;
    for(int i = 0; i<6; i++)
    {
      if(Stock[i].value>50000) Stock[i].value*=random(0.9,1.1);
      else Stock[i].value*=random(0.9,1.4);
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

void toLines()
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
