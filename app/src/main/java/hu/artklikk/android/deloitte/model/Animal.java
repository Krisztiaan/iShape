package hu.artklikk.android.deloitte.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gyozofule on 15. 11. 09..
 */
public class Animal {
    public enum AnimalType {

        Rabbit(1, Endorsement.Category.Charity),
        Panda(2, Endorsement.Category.Charity),
        Fox(3, Endorsement.Category.KnowledgeShare),
        Owl(4, Endorsement.Category.KnowledgeShare),
        Beaver(5, Endorsement.Category.Innovator),
        Monkey(6, Endorsement.Category.Innovator),
        Bee(7, Endorsement.Category.TeamBuilder),
        Dog(8, Endorsement.Category.TeamBuilder),
        Butterfly(9, Endorsement.Category.BrandBuilder),
        Parrot(10, Endorsement.Category.BrandBuilder),
        Squirrel(11, Endorsement.Category.SelfDeployment),
        Racoon(12, Endorsement.Category.SelfDeployment),
        Lion(13, Endorsement.Category.Special),
        Zebra(14, Endorsement.Category.Special),
        Unicorn(15, Endorsement.Category.Special),
        Sloth(16, Endorsement.Category.Special),
        Tiger(17, Endorsement.Category.Special),
        Chameleon(18, Endorsement.Category.Special),
        Buffalo(19, Endorsement.Category.Special);

        AnimalType (int id, Endorsement.Category category) {
            this.id = id;
            this.category = category;
        }

        private int id;
        private Endorsement.Category category;

        public int getId(){
            return id;
        }

        public static AnimalType getCategoryOfId(int id){
            for (AnimalType animal : AnimalType.values()) {
                if (animal.id == id) {
                    return animal;
                }
            }
            return null;
        }

        public static List<AnimalType> getAnimalCategoryList(Endorsement.Category categoy){
            List<AnimalType> animals = new ArrayList<>();
            for (AnimalType animal : AnimalType.values()) {
                if (animal.category.equals(categoy)) {
                    animals.add(animal);
                }
            }
            return animals;
        }

        public Endorsement.Category getCategory(){
            return this.category;
        }
    }

    public Animal(){
    }

    private int animalId;
    private String animalType;
    private int quantity;
    private String description;

    @JsonIgnore
    public AnimalType getAnimalTypeEnum() {
        return AnimalType.getCategoryOfId(animalId);
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
