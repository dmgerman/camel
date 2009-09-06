begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.soap.headers
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|soap
operator|.
name|headers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|JAXBElement
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
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|headers
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|outofband
operator|.
name|header
operator|.
name|OutofBandHeader
import|;
end_import

begin_class
DECL|class|HeaderTesterWithInsertionImpl
specifier|public
class|class
name|HeaderTesterWithInsertionImpl
extends|extends
name|HeaderTesterImpl
block|{
annotation|@
name|Override
DECL|method|validateOutOfBandHander ()
specifier|protected
name|boolean
name|validateOutOfBandHander
parameter_list|()
block|{
name|MessageContext
name|ctx
init|=
name|context
operator|==
literal|null
condition|?
literal|null
else|:
name|context
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|relayHeaders
condition|)
block|{
if|if
condition|(
name|ctx
operator|!=
literal|null
operator|&&
operator|!
name|ctx
operator|.
name|containsKey
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|||
operator|(
name|ctx
operator|.
name|containsKey
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|&&
operator|(
operator|(
name|List
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
operator|)
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
name|boolean
name|success
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|ctx
operator|!=
literal|null
operator|&&
name|ctx
operator|.
name|containsKey
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
condition|)
block|{
name|List
name|oobHdr
init|=
operator|(
name|List
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|oobHdr
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"test failed expected 2 soap headers but found "
operator|+
name|oobHdr
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|verifyHeader
argument_list|(
name|oobHdr
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"testOobHeader"
argument_list|,
literal|"testOobHeaderValue"
argument_list|)
expr_stmt|;
name|verifyHeader
argument_list|(
name|oobHdr
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"New_testOobHeader"
argument_list|,
literal|"New_testOobHeaderValue"
argument_list|)
expr_stmt|;
name|oobHdr
operator|.
name|clear
argument_list|()
expr_stmt|;
name|success
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"MessageContext is null or doesnot contain OOBHeaders"
argument_list|)
throw|;
block|}
return|return
name|success
return|;
block|}
DECL|method|verifyHeader (Object hdr, String headerName, String headerValue)
specifier|private
name|void
name|verifyHeader
parameter_list|(
name|Object
name|hdr
parameter_list|,
name|String
name|headerName
parameter_list|,
name|String
name|headerValue
parameter_list|)
block|{
if|if
condition|(
name|hdr
operator|instanceof
name|Header
operator|&&
operator|(
operator|(
name|Header
operator|)
name|hdr
operator|)
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Node
condition|)
block|{
name|Header
name|hdr1
init|=
operator|(
name|Header
operator|)
name|hdr
decl_stmt|;
try|try
block|{
name|JAXBElement
name|job
init|=
operator|(
name|JAXBElement
operator|)
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|outofband
operator|.
name|header
operator|.
name|ObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
operator|(
name|Node
operator|)
name|hdr1
operator|.
name|getObject
argument_list|()
argument_list|)
decl_stmt|;
name|OutofBandHeader
name|ob
init|=
operator|(
name|OutofBandHeader
operator|)
name|job
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|headerName
operator|.
name|equals
argument_list|(
name|ob
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"test failed expected name ' + headerName + ' but found '"
operator|+
name|ob
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|headerValue
operator|.
name|equals
argument_list|(
name|ob
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"test failed expected name ' + headerValue + ' but found '"
operator|+
name|ob
operator|.
name|getValue
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"test failed"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"test failed. Unexpected type "
operator|+
name|hdr
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

