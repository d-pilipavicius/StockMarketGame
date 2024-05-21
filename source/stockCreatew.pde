void writeStock()
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
