package kr.arawn.springframework.data.sqlmap.repository.sample;

import java.util.List;

public class SpringSprout {
    
    private Long id;
    private String name;
    private String nickName;
    private List<Language> languages;
    
    public SpringSprout(){}
    
    public SpringSprout(String name, String nickName) {
        this.name = name;
        this.nickName = nickName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public List<Language> getLanguages() {
        return languages;
    }
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
    
}
