# arXiv_OAI_PMH
arXiv Harvester extracts the archive files from arXiv in Dublin Core format. The code takes 3 inputs:

1. start_date - The date from which the archives need to be extracted
2. xmlFileLocation - The location in which the response xml from the arXiv is stored
3. recordsLocation - The location in which the records that are split from the original xml are stored.

For example we run the jar file in the command line as follows:
C:\Users\src>java -jar arxiv_OAI_PMH.jar "2016-01-05" "G:/newfile.xml" "G:/arXiv/"
