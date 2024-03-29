package com.example.mymarket.Model;
public class Article {

    private int idAliment;
    private String intitule;
    private float prix;
    private int quantite;

    private String adrImage;

    public Article()
    {
        setidAliment(-1);
        setintitule("Vide");
        setprix(0);
        setquantite(0);
        setAdrImage("Vide");
    }

    public Article(int Al , String inti , float p , int q, String ad)
    {
        setidAliment(Al);
        setintitule(inti);
        setprix(p);
        setquantite(q);
        setAdrImage(ad);
    }

    public void setidAliment(int idAliment){
        this.idAliment = idAliment;
    }

    public void setintitule(String intitule){
        this.intitule = intitule;
    }

    public void setprix(float prix) {
        this.prix = prix;
    }

    public void setquantite(int quantite) {
        this.quantite = quantite;
    }

    public void setAdrImage(String adrImage){
        this.adrImage = adrImage;
    }

    public int getIdAliment() {
        return idAliment;
    }

    public String getIntitule(){
        return intitule;
    }

    public float getPrix() {
        return prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public String getAdrImage(){
        return adrImage;
    }

    @Override
    public String toString() {
        int espaceRestant = 20 - getIntitule().length();
        return    String.format("%" + espaceRestant + "s %s %3d pcs %7.2f€ ", "", getIntitule(), getQuantite(), getPrix() * getQuantite());
    }
    public String toStringBag() {
        int espaceRestant = 20 - getIntitule().length(); // 25 est la largeur totale souhaitée
        System.out.println(espaceRestant);
        return String.format("%" + espaceRestant + "s %s %3d pcs %7.2f€ => %6.2f€/pcs", "", getIntitule(), getQuantite(), getPrix() * getQuantite(), getPrix());
    }
}
