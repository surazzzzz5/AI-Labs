import java.lang.*;
ConfigObject bMatrix = new ConfigObject()  //multidimensional map
ConfigObject termCMatrix = new ConfigObject()  //multidimensional map
ConfigObject tf = new ConfigObject() //term frequency
ConfigObject w = new ConfigObject() //weight

def df = [:] //document frequency
def idf = [:] //inverse document frequency
//assert co.one.two.equals('something')

def doc1 = ['i','do','not','like','them','sam','i','am']//datas of document 1
def doc2 = ['i','do','not','like','green','eggs','and','ham']//datas of docuent 2
def query  =['sam','i','am']//datas of query

def j = i = 0
def union  = doc1.plus(doc2).plus(query).unique { a, b -> a <=> b }//all terms that appear in doc1 doc2 and query
def N = 3 //no of document//no of total document

//cretes a binary matrix
union.each{
    if(doc1.contains(it))
        bMatrix['doc1'][it] = 1
    else
        bMatrix['doc1'][it] = 0
    if(doc2.contains(it))
        bMatrix['doc2'][it] = 1
    else
        bMatrix['doc2'][it] = 0
    if(query.contains(it))
        bMatrix['query'][it] = 1
    else
        bMatrix['query'][it] = 0
}

//creates term count matrix
union.each{
    termCMatrix['doc1'][it] = doc1.count(it)
    termCMatrix['doc2'][it] = doc2.count(it)
    termCMatrix['query'][it] = query.count(it)
}


maxDoc1 = termCMatrix['doc1'].max{it.value}.value //maximum no of count in doc1
maxDoc2 = termCMatrix['doc2'].max{it.value}.value //maximum no of count in doc2
maxQuery = termCMatrix['query'].max{it.value}.value //maximum no of count in query

//creates term frequency matrix
union.each{
    tf['doc1'][it] =  termCMatrix['doc1'][it]/maxDoc1
    tf['doc2'][it] =  termCMatrix['doc2'][it]/maxDoc2
    tf['query'][it] =  termCMatrix['query'][it]/maxQuery
}

//no of document where there is presence of the given term
union.each{
    df[it] = bMatrix['doc1'][it] + bMatrix['doc2'][it] + bMatrix['query'][it]
}

//inverse of document frequency
union.each{
    idf[it] = Math.log(N/df[it])
}

//tf-idf weighteining
union.each{
    w['doc1'][it] = tf['doc1'][it] * idf[it]
    w['doc2'][it] = tf['doc2'][it] * idf[it]
    w['query'][it] = tf['query'][it] * idf[it]
}

//function that calculates the cosine similarity of w2 text with w1
def cosine(w1,w2,union){

    def temp1 = 0
    def temp2 = 0.0
    def temp3 = 0.0

    union.each{
        temp1 = w1[it]*w2[it]+temp1
    }

    w1.each{
        temp2 = temp2 + (it.value * it.value)
    }

    w2.each{
        temp3 = temp3 + (it.value * it.value)
    }
    return temp1/(Math.sqrt(temp2)*Math.sqrt(temp3))
}


def cosine1 = cosine(w['doc1'],w['query'],union)
def cosine2 = cosine(w['doc2'],w['query'],union)

println "Cosine of doc 1 is "+cosine1+" Cosine of doc 2 is "+cosine2;
if(cosine1>cosine2){
    println "Query is more similar with Document 1"
}else{
    println "Query is more similar with Document 2"
}