int main ( ) {
  int sum;
  int divisor;
  int low;
  int high;
  {
    int value;
    sum = 0;
    divisor = 5;
    low = 1;
    high = 100;
    value = low;
    while( value <= high ) {
      bool isInputDivisibleByDivisor;
      {
        int input;
        {
          int check;    
          input = value;
          check = divisor;
          while( check < input ) {
            check = check + divisor;
          }
        isInputDivisibleByDivisor = check == input;
        }
      }
      if( isInputDivisibleByDivisor ) {
        sum = sum + value;
      }
      value = value + 1;
    }
  }
}