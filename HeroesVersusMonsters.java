// Gene Hyde
// CSCD 211-02
// HeroesVersusMonsters
// 4/10/14
// Extra credit attempted

import java.util.*;

public class HeroesVersusMonsters
{
   public static void main(String ... args)
   {
      Hero hero = chooseHero();
      
      game(hero);
   }
   
   public static void game(Hero hero)//basic game sturcutre
   {
      int cont = 1;
      Scanner kb = new Scanner(System.in);
      
      while(cont == 1 || cont == 2)
      {
         battle(hero); // single battle
         System.out.println("Continue? (Hp will be restored)\n\t1. Yes\n\t2. New Game \n\t3. No");
         try
         {
            cont = kb.nextInt();
            if(!(cont == 1 || cont == 2 || cont == 3))
               throw new Exception();
         }
         catch(Exception e)
         {
            System.out.println("Please enter 1, 2 or 3.");
            kb.nextLine();
         }
         
         if(cont == 1)
         {
            hero.setHp(hero.getMaxHp());// hp restored and hero is keeped
         }
         else if(cont == 2)// new hero is made
         {
            hero = chooseHero();
         }
      }
      
      
   }
   
   private static void battle(Hero hero)
   {
      Monster mon = monGen();// grabs a new monster
      int win = 0;
      
      hero.setTurns(hero.getAtkSpd()/mon.getAtkSpd());// gets the # of turns
      while(hero.getHp() > 0 && mon.getHp() > 0)
      {
         int atks = hero.getTurns();
         
         do
         {
            System.out.println("You have " + atks + " turn(s) left.");
            chooseAtk(hero, mon);
            if(hero.getHp() < 0)
               break;
            atks--;
         }while(atks > 0);
         
         if(hero.getHp() < 0)
         {
            System.out.println(hero + " ran crying to their mommy!");
            break;
         }
            
         if(mon.getHp() <= 0)
         {
            System.out.println(mon + " has been defeted!");
            win = 1;
            break;
         }
         mon.heal();
         mon.hp();
         mon.chooseatk(hero);       
         if(hero.getHp() <= 0)
         {
            System.out.println(hero + " has fallen!");
            win = 2;
            break;
         }
         hero.hp();
      }
     
      if(win == 1)
         System.out.println(hero + " is victorious!");
      else if(win == 2)
         System.out.println(mon + " is victorious!");         
   
   }
   
   public static Hero chooseHero()
   {
      int option = 0;
      Scanner kb = new Scanner(System.in);
      Hero hero;
      
      while(option != 1 && option != 2 && option != 3 && option != 4)
      {
         System.out.println("Choose a Hero!\n1. Warrior. (High hp and attack,"
                       + " low speed)\n\tSpecial Skill: Crushing Blow (High attack low accuracy)"
                       + "\n2. Sorceress. (Medium stats, low acuracy)\n\t"
                       + "Special Skill: Heal (Recover Hp)\n3. Thief. (High speed, low attack)"
                       + "\n\tSpecial Skill: Sneak Attack (Chance to attack twice)"
                       + "\n4. Fairy. (Low Hp, fast and hard to hit)\n\t Special"
                       + " Skill: Natures Power (Increase damage)");
                       
         try
         {
            option = kb.nextInt();
            if(!(option == 1 || option == 2 || option == 3 || option == 4))
               throw new Exception();
            break;
         }
         catch(Exception e)
         {
            System.out.println("Please enter 1, 2, 3, or 4.");
            kb.nextLine();
         } 
      }
      
      if(option == 2)
      {
         hero = new Sorceress();
      }
      else if(option == 3)
      {
         hero = new Thief();
      }
      else if(option == 4)
      {
         hero = new Fairy();
      }
      else
      {
         hero = new Warrior();
      }
      
      return hero;        
   }
   
   public static Monster monGen()
   {
      double r = Math.random();
      Monster m;
      if(r < .4)
      {
         System.out.println("A Gremlin appeared!");
         m = new Gremlin();
      }
      else if(r >= .4 && r < .8)
      {
         System.out.println("A Skeleton appeared!");
         m = new Skeleton();
      }
      else
      {
         System.out.println("An Ogre appeared!");
         m = new Ogre();
      }
      
      return m;
   }
   
   public static void chooseAtk(Hero hero, Monster m)
   {
      int option = 0;
      Scanner kb = new Scanner(System.in);
      
      while(option != 1 && option != 2 && option != 3)
      {
         System.out.println("Choose an attack.\n\t1. Basic attack.");
         if(hero.getClass().getSimpleName().equals("Warrior"))
            System.out.println("\t2. Crushing Blow.");
         else if(hero.getClass().getSimpleName().equals("Sorceress"))
            System.out.println("\t2. Heal.");
         else if(hero.getClass().getSimpleName().equals("Thief"))
            System.out.println("\t2. Suprise Attack.");
         else if(hero.getClass().getSimpleName().equals("Fairy"))
            System.out.println("\t2. Natures Power.");
         System.out.println("\t3. Quit!");
         try
         {
            option = kb.nextInt();
            if(!(option == 1 || option == 2 || option == 3))
               throw new Exception();
            break;
         }
         catch(Exception e)
         {
            System.out.println("Please enter 1, 2, or 3.");
            kb.nextLine();
         }
      }
      if(option == 1)
      {
         hero.attack(m);
      }
      else if(option == 2)
      {
         hero.spAttack(m);
      }
      else if(option == 3)
      {
         hero.setHp(-1);
      }
   }
   

}