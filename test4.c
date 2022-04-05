int main ( ) {
  int input;
  int divisor;
  bool isInputDivisibleByDivisor;
  {
    int check;    
    input = 40;
    divisor = 5;
    check = divisor;
    while( check < input ) {
      check = check + divisor;
    }
    isInputDivisibleByDivisor = check == input;
  }
}
