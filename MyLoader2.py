__author__ = 'Bailiang_Baby'
from py2neo import authenticate, Graph
from py2neo import Node, Relationship
# set up authentication parameters
authenticate("localhost:7474", "neo4j", "neo")

# connect to authenticated graph database
graph = Graph("http://localhost:7474/db/data/")


counter = 1
for line in open('dblp_proccessed.csv'):
    elements = line.split("|")
    name = elements[3]
    # mdate = elements[2]
    key = elements[1]
    author = elements[2]
    pages = elements[4]
    year = elements[5]
    volume = elements[6]
    journal = elements[7]
    number = elements[8]
    url = elements[9]
    ee = elements[10]
    cite = elements[11]
    paper = Node("Paper", title=name)
    print "paper created", counter
    counter = counter + 1

    # paper.properties["mdate"] = mdate
    paper.properties["key"] = key
    paper.properties["pages"] = pages
    paper.properties["year"] = int(year)
    paper.properties["volume"] = int(volume)
    paper.properties["journal"] = journal
    paper.properties["number"] = number
    paper.properties["url"] = url
    paper.properties["ee"] = ee
    paper.properties["cite"] = cite

    authors = author.split("^")
    cites = cite.split("^")

    lastAuthor = None
    lastCite = None
    # Current paper only has ONE author
    if(len(authors) == 1):

        print "Just 1 author  "
        oldAuthor = graph.find_one("Author", "name", authors[0])

        if (oldAuthor) is None:
            print "Creating new author"
            oldAuthor = Node("Author", name=authors[0])
        else:
            print "Find the existed author"

        author_publish_paper = Relationship(oldAuthor, "PUBLISH", paper)
        graph.create(author_publish_paper)
    # Current paper has multiple authors
    else:
        for i in range(len(authors)): # current paper author list
            j = i + 1

            fromAuthor = graph.find_one("Author", "name", authors[i])
            if fromAuthor is None:
                    fromAuthor = Node("Author", name=authors[i])
            author_publish_paper = Relationship(fromAuthor, "PUBLISH", paper)
            graph.create(author_publish_paper)

            while (j < len(authors)):

                toAuthor = graph.find_one("Author", "name", authors[j])
                if toAuthor is None:
                    toAuthor = Node("Author", name=authors[j])

                j = j + 1

                graph.create(toAuthor)
                ifExist = graph.match_one(fromAuthor, "CO", toAuthor)

                if(ifExist is None):
                    fromAuthor_To_toAuthor = Relationship(fromAuthor, "CO", toAuthor)
                    toAuthor_To_fromAuthor = Relationship(toAuthor, "CO", fromAuthor)
                    graph.create(fromAuthor_To_toAuthor)
                    graph.create(toAuthor_To_fromAuthor)
                else:
                    continue


