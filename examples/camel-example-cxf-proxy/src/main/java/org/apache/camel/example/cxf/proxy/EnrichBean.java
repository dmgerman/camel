begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.proxy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|proxy
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * A bean to enrich the proxied web service to ensure the input is valid and add additional information  */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_class
DECL|class|EnrichBean
specifier|public
class|class
name|EnrichBean
block|{
DECL|method|enrich (Document doc)
specifier|public
name|Document
name|enrich
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|Node
name|node
init|=
name|doc
operator|.
name|getElementsByTagName
argument_list|(
literal|"incidentId"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|incident
init|=
name|node
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
comment|// here we enrich the document by changing the incident id to another value
comment|// you can of course do a lot more in your use-case
name|node
operator|.
name|setTextContent
argument_list|(
literal|"456"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Incident was "
operator|+
name|incident
operator|+
literal|", changed to 456"
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

