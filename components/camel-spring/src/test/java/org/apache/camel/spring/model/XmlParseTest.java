begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|model
package|;
end_package

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|XmlParseTest
specifier|public
class|class
name|XmlParseTest
extends|extends
name|XmlTestSupport
block|{
DECL|method|testParseExample1Xml ()
specifier|public
name|void
name|testParseExample1Xml
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseAsJaxb
argument_list|(
literal|"example1.xml"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseProcessorXml ()
specifier|public
name|void
name|testParseProcessorXml
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseAsJaxb
argument_list|(
literal|"processor.xml"
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseProcessorWithFilterXml ()
specifier|public
name|void
name|testParseProcessorWithFilterXml
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseAsJaxb
argument_list|(
literal|"processorWithFilter.xml"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

