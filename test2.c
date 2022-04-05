int main ( ) {
  int baseRaisedToExp;
  int exp;
  int base;
  {
    exp = 3;    
    base = 2;
    baseRaisedToExp = 1;
    while (exp > 0) {
      baseRaisedToExp = baseRaisedToExp * base;
      exp = exp - 1;
    }
  }
}
