begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|Max
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|Min
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|NotNull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|Size
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|CMConfiguration
specifier|public
class|class
name|CMConfiguration
block|{
annotation|@
name|NotNull
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|productToken
specifier|private
name|String
name|productToken
decl_stmt|;
annotation|@
name|NotNull
annotation|@
name|Size
argument_list|(
name|min
operator|=
literal|1
argument_list|,
name|max
operator|=
literal|11
argument_list|)
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|defaultFrom
specifier|private
name|String
name|defaultFrom
decl_stmt|;
annotation|@
name|Min
argument_list|(
literal|1
argument_list|)
annotation|@
name|Max
argument_list|(
literal|8
argument_list|)
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"8"
argument_list|,
name|javaType
operator|=
literal|"int"
argument_list|)
DECL|field|defaultMaxNumberOfParts
specifier|private
name|int
name|defaultMaxNumberOfParts
init|=
literal|8
decl_stmt|;
annotation|@
name|UriParam
DECL|field|testConnectionOnStartup
specifier|private
name|boolean
name|testConnectionOnStartup
decl_stmt|;
DECL|method|getProductToken ()
specifier|public
name|String
name|getProductToken
parameter_list|()
block|{
return|return
name|productToken
return|;
block|}
comment|/**      * The unique token to use      */
DECL|method|setProductToken (String productToken)
specifier|public
name|void
name|setProductToken
parameter_list|(
name|String
name|productToken
parameter_list|)
block|{
name|this
operator|.
name|productToken
operator|=
name|productToken
expr_stmt|;
block|}
DECL|method|getDefaultFrom ()
specifier|public
name|String
name|getDefaultFrom
parameter_list|()
block|{
return|return
name|defaultFrom
return|;
block|}
comment|/**      * This is the sender name. The maximum length is 11 characters.      */
DECL|method|setDefaultFrom (final String defaultFrom)
specifier|public
name|void
name|setDefaultFrom
parameter_list|(
specifier|final
name|String
name|defaultFrom
parameter_list|)
block|{
name|this
operator|.
name|defaultFrom
operator|=
name|defaultFrom
expr_stmt|;
block|}
DECL|method|getDefaultMaxNumberOfParts ()
specifier|public
name|int
name|getDefaultMaxNumberOfParts
parameter_list|()
block|{
return|return
name|defaultMaxNumberOfParts
return|;
block|}
comment|/**      * If it is a multipart message forces the max number. Message can be truncated.      * Technically the gateway will first check if a message is larger than 160 characters,      * if so, the message will be cut into multiple 153 characters parts limited by these parameters.      */
DECL|method|setDefaultMaxNumberOfParts (final int defaultMaxNumberOfParts)
specifier|public
name|void
name|setDefaultMaxNumberOfParts
parameter_list|(
specifier|final
name|int
name|defaultMaxNumberOfParts
parameter_list|)
block|{
name|this
operator|.
name|defaultMaxNumberOfParts
operator|=
name|defaultMaxNumberOfParts
expr_stmt|;
block|}
DECL|method|isTestConnectionOnStartup ()
specifier|public
name|boolean
name|isTestConnectionOnStartup
parameter_list|()
block|{
return|return
name|testConnectionOnStartup
return|;
block|}
comment|/**      * Whether to test the connection to the SMS Gateway on startup      */
DECL|method|setTestConnectionOnStartup (final boolean testConnectionOnStartup)
specifier|public
name|void
name|setTestConnectionOnStartup
parameter_list|(
specifier|final
name|boolean
name|testConnectionOnStartup
parameter_list|)
block|{
name|this
operator|.
name|testConnectionOnStartup
operator|=
name|testConnectionOnStartup
expr_stmt|;
block|}
block|}
end_class

end_unit

