// Gene Hyde
// CSCD 211-02
// HeroesVersusMonsters
// 4/10/14
// Extra credit attempted

import java.util.*;

public class Warrior extends Hero
{
   public Warrior()
   {
      super("Hero", 125, 4, 60, 35, 0.80, 0.2);
   }

   public void spAttack(Character m)// special attack
   {
      Random randomNum = new Random();
      int hit;
      System.out.println("You prepare to swing your sword with both hands and...");
      if(Math.random() <= .4)
      {
         do 
         {
            hit = randomNum.nextInt(176);
         }while(hit <= 75);
         m.takedmg(hit);
      }
      else
      {
         System.out.println(m + " avoided your attack");
      }
   }
   
   @Override
   public void attack(Monster mon)// class specific basic attack
   {
      Random randomNum = new Random();
      int hit;
      
      System.out.println("You swing your sword and...");

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
}