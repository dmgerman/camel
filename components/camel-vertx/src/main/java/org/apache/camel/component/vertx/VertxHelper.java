begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vertx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vertx
package|;
end_package

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|json
operator|.
name|JsonArray
import|;
end_import

begin_import
import|import
name|io
operator|.
name|vertx
operator|.
name|core
operator|.
name|json
operator|.
name|JsonObject
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
name|Message
import|;
end_import

begin_class
DECL|class|VertxHelper
specifier|public
specifier|final
class|class
name|VertxHelper
block|{
DECL|method|VertxHelper ()
specifier|private
name|VertxHelper
parameter_list|()
block|{     }
DECL|method|getVertxBody (Exchange exchange)
specifier|public
specifier|static
name|Object
name|getVertxBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|msg
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|body
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|JsonObject
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|body
operator|=
name|msg
operator|.
name|getBody
argument_list|(
name|JsonArray
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|body
operator|=
name|msg
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|body
return|;
block|}
block|}
end_class

end_unit

