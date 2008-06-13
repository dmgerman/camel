begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|Endpoint
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
name|component
operator|.
name|spring
operator|.
name|integration
operator|.
name|SpringIntegrationEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|channel
operator|.
name|MessageChannel
import|;
end_import

begin_comment
comment|/**  * The<a href="http://activemq.apache.org/camel/type-converter.html">Type Converters</a>  * for turning the Spring Integration types into Camel native type.  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|SpringIntegrationConverter
specifier|public
specifier|final
class|class
name|SpringIntegrationConverter
block|{
DECL|method|SpringIntegrationConverter ()
specifier|private
name|SpringIntegrationConverter
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * @param Spring Integration MessageChannel      * @return an Camel Endpoint      * @throws Exception      */
annotation|@
name|Converter
DECL|method|toEndpoint (final MessageChannel channel)
specifier|public
specifier|static
name|Endpoint
name|toEndpoint
parameter_list|(
specifier|final
name|MessageChannel
name|channel
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|channel
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The MessageChannel is null"
argument_list|)
throw|;
block|}
name|Endpoint
name|answer
init|=
operator|new
name|SpringIntegrationEndpoint
argument_list|(
literal|"URL"
argument_list|,
name|channel
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// check the channel
return|return
name|answer
return|;
block|}
comment|//TODO add the message and endpoint type converter
block|}
end_class

end_unit

