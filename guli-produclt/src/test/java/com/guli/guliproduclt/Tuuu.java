package com.guli.guliproduclt;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/4
 */
public class Tuuu {
        public static void main(String[] args) {
            IPerson person = new Man();

            System.out.println("----------------------");
            System.out.println("增加裤子适配器");
            person = new TrousersDecorator(person);
//
            System.out.println("----------------------");
            System.out.println("再增加内衣适配器");
            person = new UnderClothesDecorator(person);

            System.out.println("----------------------");
            System.out.println("再增加外套适配器");
            person = new OvercoatDecorator(person);
            person.dress();
        }

        // 抽象组件（Component）
        interface IPerson {
            void dress();
        }

        // 具体组件（ConcreteComponent）,即被修饰者
        static class Man implements IPerson {

            @Override
            public void dress() {
                System.out.println("穿了内裤!");
            }
        }

        // 抽象适配器（Decorator）,接收一个具体的Component，本身也是一个Component
        static abstract class ClothesDecorator implements IPerson {
            protected IPerson mPerson;

            // 构造方法强制子类构造必须传入一个IPerson
            public ClothesDecorator(IPerson person) {
                this.mPerson = person;
            }

            @Override
            public void dress() {
                this.mPerson.dress();
            }
        }

        //具体装饰器（ConcreteDecorator）：裤子装饰器
        static class TrousersDecorator extends ClothesDecorator {

            public TrousersDecorator(IPerson person) {
                super(person);
            }

            @Override
            public void dress() {
                super.dress();
                this.dressTrousers();
            }

            private void dressTrousers() {
                System.out.println("穿上裤子了!");
            }
        }

        //具体装饰器（ConcreteDecorator）：内衣装饰器
        static class UnderClothesDecorator extends ClothesDecorator {
            public UnderClothesDecorator(IPerson person){
                super(person);
            }

            @Override
            public void dress() {
                super.dress();
                this.dressUnderClothes();
            }

            private void dressUnderClothes(){
                System.out.println("穿上内衣了!");
            }
        }

        //具体装饰器（ConcreteDecorator）：外套装饰器
        static class OvercoatDecorator extends ClothesDecorator {
            public OvercoatDecorator(IPerson person){
                super(person);
            }

            @Override
            public void dress() {
                super.dress();
                this.dressOvercoat();
            }

            private void dressOvercoat(){
                System.out.println("穿上外套了!");
            }
        }




}
