package com.example.freshmanfinal;

public class AllTrainers {


        public String Room , Title,Trainer,Date;

        public AllTrainers()
        {

        }
        public AllTrainers(String Room, String Title,String Trainer, String Date) {
            this.Room = Room;
            this.Title = Title;
            this.Trainer = Trainer;
            this.Date = Date;

        }

        public String getTR_Title() {


            return Title;
        }

        public void set_Tile(String Title) {
            this.Title = Title;
        }

        public String get_Room() { return Room; }

        public void set_Room(String Room) {
            this.Room = Room;
        }

    public String get_Date() { return Date; }

    public void set_Date(String Date) {
        this.Date = Date;
    }

       public String get_Trainer() { return Trainer; }

       public void set_Trainer(String Trainer) {
        this.Trainer = Trainer;
    }




    }
