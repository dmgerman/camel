begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|XmlTestSupport
specifier|public
class|class
name|XmlTestSupport
extends|extends
name|TestSupport
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|jaxbContext
specifier|protected
name|JAXBContext
name|jaxbContext
decl_stmt|;
DECL|method|assertParseAsJaxb (String uri)
specifier|protected
name|RouteContainer
name|assertParseAsJaxb
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Object
name|value
init|=
name|parseUri
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|RouteContainer
name|context
init|=
name|assertIsInstanceOf
argument_list|(
name|RouteContainer
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Found: "
operator|+
name|context
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|parseUri (String uri)
specifier|protected
name|Object
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot find resource on the classpath: "
operator|+
name|uri
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|resource
argument_list|)
decl_stmt|;
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.camel.model:org.apache.camel.model.language"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

