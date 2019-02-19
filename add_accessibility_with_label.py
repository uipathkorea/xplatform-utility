#-*- coding: utf-8 -*- 
# 작성자: 김형수( hyungsoo.kim@uipath.com) 

import xml.etree.ElementTree as ET
import argparse
import os

#accessibility label을 설정할 Tag 이름 
TAGS = ['Edit','Radio', 'Combo', 'Button', 'Tabpage', 'Calendar', 'TextArea', 'Dataset']

parser = argparse.ArgumentParser(description='add accessibility style with label from element id attribute')
parser.add_argument('--file', dest='file', help='xpltform xfdl file')
parser.add_argument('--out', dest='out', help='output file')
args = parser.parse_args()

tree = ET.parse( args.file )
e = tree.getroot() #element instance 생성

for tag in TAGS: 
    for i in e.iter( tag):
        if 'id' in i.attrib:
            if 'style' in i.attrib : # 다른 style 이 있는 경우 
                if 'accessibility:enable' not in i.attrib['style']: #  accessibility가 설정되지 않은 경우에만 
                    i.attrib['style'] = i.attrib['style']  + ";accessibility:enable all '" + i.attrib['id'] + "' '';"
            else : # style attribute가 없는 경우 
                i.attrib['style'] = "accessibility:enable all '" + i.attrib['id'] + "' '';"

if args.out is not None:
    tree.write( args.out, xml_declaration=True, encoding='UTF-8')
else:
    tree.write( 'updated_' + os.path.basename(args.file), xml_declaration=True, encoding='UTF-8')
