begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_comment
comment|/**  * Represents the result of a call to the<i>setWebhook</i> REST service.  */
end_comment

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|WebhookResult
specifier|public
class|class
name|WebhookResult
block|{
DECL|field|ok
specifier|private
name|boolean
name|ok
decl_stmt|;
DECL|field|result
specifier|private
name|boolean
name|result
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|method|WebhookResult ()
specifier|public
name|WebhookResult
parameter_list|()
block|{     }
DECL|method|isOk ()
specifier|public
name|boolean
name|isOk
parameter_list|()
block|{
return|return
name|ok
return|;
block|}
DECL|method|setOk (boolean ok)
specifier|public
name|void
name|setOk
parameter_list|(
name|boolean
name|ok
parameter_list|)
block|{
name|this
operator|.
name|ok
operator|=
name|ok
expr_stmt|;
block|}
DECL|method|isResult ()
specifier|public
name|boolean
name|isResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
DECL|method|setResult (boolean result)
specifier|public
name|void
name|setResult
parameter_list|(
name|boolean
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"WebhookResult{"
operator|+
literal|"ok="
operator|+
name|ok
operator|+
literal|", result="
operator|+
name|result
operator|+
literal|", description='"
operator|+
name|description
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

