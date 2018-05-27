var contract = function contract(dObj) {
    //print(arguments[0].attr + ", " +  arguments[1].attr);
    //Crude physics: force of muscle contraction is 1 minus resistance of indirect object
    dObj.attr = 1;
    //print(dObj.attr);
    return dObj.attr;
}
contract.params = ["dObj"];
//print(contract.length);
