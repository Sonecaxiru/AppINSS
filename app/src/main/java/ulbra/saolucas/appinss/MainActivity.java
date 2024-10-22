package ulbra.saolucas.appinss;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finding views by their ID
        EditText nomeEditText = findViewById(R.id.ednome);
        EditText salarioEditText = findViewById(R.id.edsalario);
        EditText filhosEditText = findViewById(R.id.edfilhos);
        RadioGroup sexoRadioGroup = findViewById(R.id.rgsexo);
        Button calculateButton = findViewById(R.id.btcalcular);
        TextView resultINSS = findViewById(R.id.resultINSS);
        TextView resultIR = findViewById(R.id.resultIR);
        TextView resultNetSalary = findViewById(R.id.resultNetSalary);
        TextView resultFamilySalary = findViewById(R.id.resultFamilySalary); // New field for family salary

        // Setting an OnClickListener for the calculate button
        calculateButton.setOnClickListener(v -> {
            // Retrieving input values
            String nome = nomeEditText.getText().toString();
            Double salario = parseDouble(salarioEditText.getText().toString());
            Integer filhos = parseInt(filhosEditText.getText().toString());
            int selectedSexoId = sexoRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedSexo = findViewById(selectedSexoId);
            String sexo = selectedSexo.getText().toString();

            // Validation checks
            if (salario == null || salario < 0) {
                Toast.makeText(this, "Salário inválido!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (filhos == null || filhos < 0) {
                Toast.makeText(this, "Número de filhos inválido!", Toast.LENGTH_SHORT).show();
                return;
            }

            // INSS Calculation
            double inss = calculateINSS(salario);
            // IR Calculation
            double ir = calculateIR(salario);
            // Family Salary Calculation (R$ 56.47 per child if gross salary is <= R$ 1,212.00)
            double familySalary = calculateFamilySalary(salario, filhos);
            // Net Salary Calculation
            double netSalary = salario - inss - ir + familySalary;

            // Display results
            String prefix = sexo.equals("Masculino") ? "Sr." : "Sra.";
            resultINSS.setText(String.format("INSS R$: %.2f", inss));
            resultIR.setText(String.format("IR R$: %.2f", ir));
            resultFamilySalary.setText(String.format("Salário Família R$: %.2f", familySalary)); // Display family salary
            resultNetSalary.setText(String.format("%s %s, Salário Líquido R$: %.2f", prefix, nome, netSalary));
        });
    }

    // Method to calculate INSS based on salary
    private double calculateINSS(double salario) {
        if (salario <= 1212.00) {
            return salario * 0.075;
        } else if (salario <= 2427.35) {
            return salario * 0.09;
        } else if (salario <= 3641.03) {
            return salario * 0.12;
        } else if (salario <= 7087.22) {
            return salario * 0.14;
        } else {
            return 0.0;
        }
    }

    // Method to calculate IR based on salary
    private double calculateIR(double salario) {
        if (salario <= 1903.98) {
            return 0.0;
        } else if (salario <= 2826.65) {
            return salario * 0.075;
        } else if (salario <= 3751.05) {
            return salario * 0.15;
        } else if (salario <= 4664.68) {
            return salario * 0.225;
        } else {
            return salario * 0.275;
        }
    }

    // Method to calculate family salary
    private double calculateFamilySalary(double salario, int filhos) {
        if (salario <= 1212.00) {
            return filhos * 56.47;
        } else {
            return 0.0;
        }
    }

    // Helper method to safely parse a double
    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails
        }
    }

    // Helper method to safely parse an integer
    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails
        }
    }
}
