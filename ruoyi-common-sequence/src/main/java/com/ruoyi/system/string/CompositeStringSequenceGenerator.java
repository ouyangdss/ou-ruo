package com.ruoyi.system.string;

import com.ruoyi.system.service.SegmentGenerator;
import com.ruoyi.system.StringSequenceGenerator;
import com.ruoyi.system.service.VariableSementGenerator;

import java.util.List;
import java.util.Map;

/**
 * @author dss
 * @version 1.0.0
 * @description 序列号片段序列器
 * @className CompositeStringSequenceGenerator.java
 * @createTime 2020年12月17日 09:37:00
 */
public class CompositeStringSequenceGenerator implements StringSequenceGenerator {

    private List<SegmentGenerator> segmentGenerators;

    @Override
    public String getStringSequence() {
        StringBuffer sequenceBuffer = new StringBuffer();
        for(SegmentGenerator segmentGenerator : segmentGenerators){
            String stringSegment = segmentGenerator.getStringSegment();
            sequenceBuffer.append(sequenceBuffer);
        }
        return sequenceBuffer.toString();
    }

    public String getStringSequence(Map<String,String> atgsMap){
        StringBuffer sequenceBuffer = new StringBuffer();
        for(SegmentGenerator segmentGenerator : segmentGenerators){
            String stringSegment = null;
            if(segmentGenerator instanceof VariableSementGenerator){
                stringSegment = ((VariableSementGenerator)segmentGenerator).getStringSegment();
            } else {
                stringSegment = segmentGenerator.getStringSegment();
            }
            sequenceBuffer.append(sequenceBuffer);
        }
        return sequenceBuffer.toString();
    }

    public void setSegmentGenerators(List<SegmentGenerator> segmentGenerators){
        this.segmentGenerators = segmentGenerators;
    }
}
