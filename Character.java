// Gene Hyde
// CSCD 211-02
// HeroesVersusMonsters
// 4/10/14
// Extra credit attempted

import java.util.*;

public abstract class Character
{
   protected String name;
   private final int maxhp;
   protected int hp;
   protected int atkspd;
   protected int dmgmax;
   protected int dmgmin;
   protected double acuracy;
   
   public Character()
   {
      this.name = "ERROR"; //obscure video game refrence
      this.maxhp = 100;
      this.hp = maxhp;
      this.atkspd = 5;
      this.dmgmax = 40;
      this.dmgmin = 15;
      this.acuracy = 0.85;
   }
   
   public Character(String name,    int maxhp,      int     atkspd, 
                    int    dmgmax,  int dmgmin,  double  acuracy)
   {
      this.name = name;
      this.maxhp = maxhp;
      this.hp = maxhp;
      this.atkspd = atkspd;
      this.dmgmax = dmgmax;
      this.dmgmin = dmgmin;
      this.acuracy = acuracy;
   }
   
   public void setHp(int hp)// change the current hp
   {
      this.hp = hp;
   }
   
   public int getHp()//show the current hp
   {
      return hp;
   }
   
   public int getMaxHp()// find the Hp cap
   {
      return maxhp;
   }
   
   public int getAtkSpd()// for compareing attack speed
   {
      return atkspd;
   }
   
   public String toString()// just returns the name
   {
      return name;
   }
   
   public void attack(Character c2) // basic attack
   {
      Random randomNum = new Random();
      int hit;
      
      if(Math.random() <= acuracy)
      {
         do 
         {
            hit = randomNum.nextInt(dmgmax + 1);
         }while(hit <= dmgmin);
         c2.takedmg(hit);
      }
      else
      {
         System.out.println("The attack missed its mark...");
      }
      
   }
   
   public void takedmg(int hit) // basic hit point reduction
   {
      hp = hp - hit;
      System.out.println("The attack did " + hit + " of damage!");
   }
   
   public void hp() // displays the Hp
   {
      System.out.println(name + " has "+hp+"/"+maxhp+" Hp");
      System.out.println("--------------------------------------------------");
   }
   
   public abstract void spAttack(Character c);// for all the special attacks
   
   //Just something extra i wanted to have if i needed
   public void hpRecovery()
   {
      Random randomNum = new Random();
      int recov = randomNum.nextInt(11) + 5;
      hp = hp + recov;
      if(hp > maxhp)
         hp = maxhp;
      System.out.println(name + " wounds heal for " + recov + " Hp and now has "
                       + hp   + "/"              + maxhp + " Hp!");
   }
   
   
}
   