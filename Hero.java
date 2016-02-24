// Gene Hyde
// CSCD 211-02
// HeroesVersusMonsters
// 4/10/14
// Extra credit attempted

import java.util.*;

public abstract class Hero extends Character
{
   protected double block;
   protected int turns;
   
   public Hero()
   {
      super("Hero", 150, 5, 40, 15, 0.85);
      this.name = enterName();
      this.block = 0.15;
      this.turns = 1;
   }
   public Hero(String name,    int     maxhp,   int    atkspd,  int dmgmax,  
               int    dmgmin,  double  acuracy, double block)
   {
      super(name, maxhp, atkspd, dmgmax, dmgmin, acuracy);
      this.name = enterName();
      this.block = block;
      this.turns = 1;
   
   }
   
   public void setTurns(int turns)
   {
      this.turns = turns;
   }
   
   public int getTurns()// get the number of turns the char gets (weird var to have but whatever)
   {
      return turns;
   }
   
   @Override
   public void takedmg(int hit)//more specific damage taker for heros only
   {
         if(Math.random() >= block)
         {
            hp = hp - hit;
            System.out.println("You took " + hit + " points of damage!");
         }
         else
         {
            System.out.println("You blocked the mosters attack!");
         }
   }
   
   public void attack(Monster mon)// more specific basic attack
   {
      Random randomNum = new Random();
      int hit;
      
      if(Math.random() <= acuracy)
      {
         do 
         {
            hit = randomNum.nextInt(dmgmax + 1);
         }while(hit <= dmgmin);
         mon.takedmg(hit);
      }
      else
      {
         System.out.println(mon + " avoided your attack");
      }
      
   }
      
   public static String enterName()// asks for a new name
   {
      Scanner kb = new Scanner(System.in);
      System.out.println("Please enter your hero's name:");
      String s = kb.nextLine();
      return s;
   }

}