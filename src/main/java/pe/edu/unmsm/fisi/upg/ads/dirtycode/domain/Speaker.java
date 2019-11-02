package pe.edu.unmsm.fisi.upg.ads.dirtycode.domain;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.NoSessionsApprovedException;
import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.SpeakerDoesntMeetRequirementsException;

public class Speaker {
	private String firstName;
	private String lastName;
	private String email;
	private int yearExperience;
	private boolean hasBlog;
	private String blogURL;
	private WebBrowser browser;
	private List<String> certifications;
	private String employer;
	private int registrationFee;
	private List<Session> sessions;
        private String[] arrayTechnology;
        private List<String> arrayDomains;
        private List<String> arrayEmployer;
        private HashMap<Integer,Integer> validateRegistrationFee;
        Integer speakerId;
        
        static final int minimoYearExperience = 10;
        static final int numberCertifications = 3;
        static final String characterArroba = "@";
        
        enum Technology {   
            Cobol("Cobol"),
            Punch_Cards("Punch Cards"), 
            Commodore("Commodore"), 
            VBScrip("VBScript");
            
            private String nameTechnology;

            private Technology(String nameTechnology) {
                this.nameTechnology = nameTechnology;
            }
            
            public String getNameTechnology(){
                return nameTechnology;
            }
        };
        
        enum Domains {   
            aol("aol.com"),
            hotmail("hotmail.com"), 
            prodigy("prodigy.com"), 
            compuserve("compuserve.com");
            
            private String nameDomains;

            private Domains(String nameDomains) {
                this.nameDomains = nameDomains;
            }
            
            public String getNameDomains(){
                return nameDomains;
            }
        };
                
        enum Employer {   
            Pluralsight("Pluralsight"),
            Microsoft("Microsoft"), 
            Google("Google"), 
            Fog_Creek_Software("Fog Creek Software"),
            Signals("Signals"),
            Telerik("Telerik");
            
            private String nameEmployer;

            private Employer(String nameEmployer) {
                this.nameEmployer = nameEmployer;
            }
            
            public String getNameEmployer(){
                return nameEmployer;
            }
        };
        
        enum listaYearExperience {   
            uno(1), 
            dos(2), 
            tres(3),
            cuatro(4),
            cinco(5),
            seis(6),
            nueve(9);
            
            private int yearExperience;

            private listaYearExperience(int yearExperience) {
                this.yearExperience = yearExperience;
            }
            
            public int getYearExperience(){
                return yearExperience;
            }
        };
        
	public Integer register(IRepository repository) throws Exception {
            
            fillOutArrays();
            validateSpeaker();
            boolean isSpeakerQualified = validateSpeakerQualified();
            
            if (isSpeakerQualified) throw new SpeakerDoesntMeetRequirementsException("Speaker doesn't meet our abitrary and capricious standards.");
                	
            boolean approvedSessions = validateApprovedSessions();

            if (approvedSessions) throw new NoSessionsApprovedException("No sessions approved.");
            
            validateRegistrationFree();
            
            saveSpeaker(repository);
            
            return speakerId;
	}
        
        private void saveSpeaker(IRepository repository){
            try {
                    speakerId = repository.saveSpeaker(this);
            } catch (Exception e) {
                    //in case the db call fails 
            }
        }
        
        public void fillOutRegistrationFree(){
            validateRegistrationFee.put(1,500);
            validateRegistrationFee.put(2,250);
            validateRegistrationFee.put(4,100);
            validateRegistrationFee.put(6,50);
            validateRegistrationFee.put(7,0);
        }
        
        public void validateRegistrationFree(){
            validateRegistrationFee = new HashMap<Integer, Integer>();
            fillOutRegistrationFree();
            
            if (this.yearExperience <= listaYearExperience.uno.getYearExperience()) {
                this.registrationFee = validateRegistrationFee.get(1);
                return;
            }
            if (this.yearExperience >= listaYearExperience.dos.getYearExperience() && 
                this.yearExperience <= listaYearExperience.tres.getYearExperience()) {
                this.registrationFee = validateRegistrationFee.get(2);
                return;
            }
            if (this.yearExperience >= listaYearExperience.cuatro.getYearExperience() && 
                this.yearExperience <= listaYearExperience.cinco.getYearExperience()) {
                this.registrationFee = validateRegistrationFee.get(4);
                return;
            }
            if (this.yearExperience >= listaYearExperience.seis.getYearExperience() && 
                this.yearExperience <= listaYearExperience.nueve.getYearExperience()) {
                this.registrationFee = validateRegistrationFee.get(6);
                return;
            }
            
            this.registrationFee = validateRegistrationFee.get(7);
            return;
        }
        
        public void fillOutArrays(){
            arrayTechnology = new String[] {    Technology.Cobol.getNameTechnology(), 
                                                Technology.Commodore.getNameTechnology(), 
                                                Technology.Punch_Cards.getNameTechnology(), 
                                                Technology.VBScrip.getNameTechnology() };
                
            arrayDomains = Arrays.asList(   Domains.aol.getNameDomains(), 
                                            Domains.compuserve.getNameDomains(),
                                            Domains.hotmail.getNameDomains(),
                                            Domains.prodigy.getNameDomains() );
                
            arrayEmployer = Arrays.asList(  Employer.Fog_Creek_Software.getNameEmployer(),
                                            Employer.Google.getNameEmployer(),
                                            Employer.Microsoft.getNameEmployer(),
                                            Employer.Pluralsight.getNameEmployer(),
                                            Employer.Signals.getNameEmployer(),
                                            Employer.Telerik.getNameEmployer() );
        }
      
        private void validateSpeaker(){
            if (this.firstName.isEmpty()) throw new IllegalArgumentException("First Name is required");
            if (this.lastName.isEmpty()) throw new IllegalArgumentException("Last name is required.");
            if (this.email.isEmpty()) throw new IllegalArgumentException("Email is required.");
        }
        
        public boolean validateSpeakerQualified(){
            boolean validExperience = this.yearExperience > minimoYearExperience;
            boolean validBlog = this.hasBlog;
            boolean validCertifications = this.certifications.size() > numberCertifications;
            boolean validEmployer = arrayEmployer.contains(this.employer);
            
            boolean isSpeakerQualified = validExperience || validBlog || validCertifications || validEmployer;
        
            if (isSpeakerQualified) return false;
            
            String[] splitted = this.email.split(characterArroba);
            String emailDomain = splitted[splitted.length - 1];
            
            boolean validateDomains = arrayDomains.contains(emailDomain);
            boolean validateBrowserName = browser.getName() == WebBrowser.BrowserName.InternetExplorer;
            boolean validateMajorVersion = browser.getMajorVersion() < 9;
            
            if (validateDomains && (validateBrowserName && validateMajorVersion)) return false;
            
            return true;
        }
        
        public boolean validateApprovedSessions(){
            boolean approved = false;
            
            if (this.sessions.isEmpty()) throw new IllegalArgumentException("Can't register speaker with no sessions to present.");
            
            for (Session session : sessions) {
                for (String technology : arrayTechnology) {
                    if (session.getTitle().contains(technology) || session.getDescription().contains(technology)) {
                            session.setApproved(false);
                            break;
                    } else {
                            session.setApproved(true);
                            approved = true;
                    }
                }

            }
            
            return approved;
        }

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getExperience() {
		return yearExperience;
	}

	public void setExperience(int yearExperience) {
		this.yearExperience = yearExperience;
	}

	public boolean isHasBlog() {
		return hasBlog;
	}

	public void setHasBlog(boolean hasBlog) {
		this.hasBlog = hasBlog;
	}

	public String getBlogURL() {
		return blogURL;
	}

	public void setBlogURL(String blogURL) {
		this.blogURL = blogURL;
	}

	public WebBrowser getBrowser() {
		return browser;
	}

	public void setBrowser(WebBrowser browser) {
		this.browser = browser;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public int getRegistrationFee() {
		return registrationFee;
	}

	public void setRegistrationFee(int registrationFee) {
		this.registrationFee = registrationFee;
	}
}