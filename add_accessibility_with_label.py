#-*- coding: utf-8 -*- 
# 작성자: 김형수( hyungsoo.kim@uipath.com) 

import xml.etree.ElementTree as ET
import argparse
import os, fnmatch
import random
import sys
import tempfile

#accessibility label을 설정할 Tag 이름 
TAGS = ['Edit','Radio', 'Combo', 'Button', 'Tabpage', 'Calendar', 'TextArea', 'MaskEdit', 'CheckBox']
LABEL_FMT = "Ui-{0}-{1}"

parser = argparse.ArgumentParser(description='add accessibility style with label from element id attribute')
parser.add_argument('--file', dest='file', help='xpltform xfdl file')
parser.add_argument("--dir", dest='dir', help='directory contains xfdl files')
parser.add_argument('--overwrite', dest='overwrite', help=' yes or no')
args = parser.parse_args()


def append_label( xfdl):
    print(xfdl)
    tree = ET.parse( xfdl )
    e = tree.getroot() #element instance 생성
    label_id = random.randint(0,100)
    for tag in TAGS: 
        for i in e.iter( tag):
            if 'style' in i.attrib : # 다른 style 이 있는 경우 
                if 'accessibility:enable' not in i.attrib['style']: #  accessibility가 설정되지 않은 경우에만 
                    if i.attrib['style'].strip().endswith(';'):
                        i.attrib['style'] = i.attrib['style']  + "accessibility:enable self 'label: " +  \
                        LABEL_FMT.format(tag, label_id) + "';"
                    else:
                        i.attrib['style'] = i.attrib['style']  + ";accessibility:enable self 'label: " +  \
                        LABEL_FMT.format(tag, label_id) + "';"
            else : # style attribute가 없는 경우 
                i.attrib['style'] = "accessibility:enable self 'label: " + \
                LABEL_FMT.format( tag, label_id) + "';"
            label_id  = label_id + 1

    if args.overwrite is not None and args.overwrite in ['yes','YES','Yes']:
        tree.write( xfdl, xml_declaration=True, encoding='UTF-8')
    else:
        print(ET.tostring( tree.getroot(),encoding="UTF-8"))

if __name__ == '__main__':
    if args.dir is not None:
        files = fnmatch.filter( os.listdir( args.dir), "*.xfdl")
        for xfdl in files:
            append_label( os.path.join( args.dir, xfdl))
    elif args.file is not None:
        xfdl = args.file
        append_label(xfdl)