#-*- coding: utf-8 -*- 
# 작성자: 김형수( hyungsoo.kim@uipath.com) 

import xml.etree.ElementTree as ET
import argparse
import os

#accessibility label을 설정할 Tag 이름 
TAGS = ['Edit','Radio', 'Combo', 'Button', 'Tabpage', 'Calendar']

parser = argparse.ArgumentParser(description='add accessibility style with label from element id attribute')
parser.add_argument('--file', dest='file', help='xpltform xfdl file')
args = parser.parse_args()

tree = ET.parse( args.file )
e = tree.getroot() #element instance 생성

for tag in TAGS: 
    for i in e.iter( tag):
        if 'style' in i.attrib:
            i.attrib['style'] = i.attrib['style']  + ";accessibility:enable all '" + i.attrib['id'] + "' '';"
        else:
            i.attrib['style'] = "accessibility:enable all '" + i.attrib['id'] + "' '';"

tree.write( 'updated_' + os.path.basename(args.file))
