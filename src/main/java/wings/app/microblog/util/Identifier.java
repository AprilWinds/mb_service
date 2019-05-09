package wings.app.microblog.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Identifier {

    private Integer length;
    private String machineId;
    private Boolean avoidance;
    private static final Integer specialDigit1 = 7;
    private static final Integer specialDigit2 = 5;
    private static final Integer specialDigit3 = 3;

    public Identifier(Integer length, String machineId, Boolean avoidance){
        this.length = length;
        this.machineId = machineId;
        this.avoidance = avoidance;
    }

    public String generate(){
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {
            hashCodeV = - hashCodeV;
        }
        String firstStepCode = String.format("%0"+this.length.toString()+"d", hashCodeV);
        String secondStepCode = "";
        if(firstStepCode.length() > this.length){
            secondStepCode = firstStepCode.substring(firstStepCode.length() - this.length);
        }
        if(this.avoidance && this.codeMatchAvoidFormula(secondStepCode)) return generate();
        String finalStepCode = this.machineId + secondStepCode;
        return finalStepCode;
    }

    private Boolean codeMatchAvoidFormula(String code){
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0;i < code.length(); i++){
            numbers.add(Integer.parseInt(String.valueOf(code.charAt(i))));
        }
        int size = numbers.size();
        if(code.matches("\\d+0001$")) return true;
        if(code.matches("\\d+77$")) return true;
        if(code.matches("\\d+\\d7\\d7$")) return true;
        if(code.matches("\\d+777\\d$")) return true;
        if(numbers.get(size-1).equals(numbers.get(size-2)) && numbers.get(size-2).equals(numbers.get(size-3))) return true;
        if(numbers.get(size-1).equals(numbers.get(size-2)+1) && numbers.get(size-2).equals(numbers.get(size-3)+1) && numbers.get(size-3).equals(numbers.get(size-4)+1)) return true;
        if(numbers.get(size-1).equals(numbers.get(size-3)) && numbers.get(size-2).equals(numbers.get(size-4)) && numbers.get(size-3).equals(numbers.get(size-5))) return true;
        if(numbers.get(size-1).equals(numbers.get(size-2)) && numbers.get(size-3).equals(numbers.get(size-4))) return true;
        if(numbers.get(size-2).equals(numbers.get(size-3)) && numbers.get(size-3).equals(numbers.get(size-4)) && (numbers.get(size-1).equals(specialDigit1) || numbers.get(size-1).equals(specialDigit2) || numbers.get(size-1).equals(specialDigit3))) return true;
        if(numbers.get(size-2).equals(numbers.get(size-3)) && numbers.get(size-3).equals(numbers.get(size-4)) && numbers.get(size-4).equals(numbers.get(size-5)) && numbers.get(size-5).equals(numbers.get(size-6))) return true;
        if(code.substring(size-3).equals(code.substring(size-6, size-3))) return true;
        if(size > 7) {
            if (code.substring(size - 4).equals(code.substring(size - 8, size - 4))) return true;
        }
        return false;
    }
}
