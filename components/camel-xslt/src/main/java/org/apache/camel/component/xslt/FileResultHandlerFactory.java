begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Exchange
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
name|support
operator|.
name|ExchangeHelper
import|;
end_import

begin_comment
comment|/**  * Factory for {@link javax.xml.transform.stream.StreamResult} which is streamed to file.  */
end_comment

begin_class
DECL|class|FileResultHandlerFactory
specifier|public
class|class
name|FileResultHandlerFactory
implements|implements
name|ResultHandlerFactory
block|{
annotation|@
name|Override
DECL|method|createResult (Exchange exchange)
specifier|public
name|ResultHandler
name|createResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|fileName
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|exchange
argument_list|,
name|Exchange
operator|.
name|XSLT_FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|FileResultHandler
argument_list|(
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

