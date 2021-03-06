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
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|JsonProperty
import|;
end_import

begin_comment
comment|/**  * Represents the result of a call to<i>getUpdates</i> REST service.  */
end_comment

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|UpdateResult
specifier|public
class|class
name|UpdateResult
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4560342931918215225L
decl_stmt|;
DECL|field|ok
specifier|private
name|boolean
name|ok
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"result"
argument_list|)
DECL|field|updates
specifier|private
name|List
argument_list|<
name|Update
argument_list|>
name|updates
decl_stmt|;
DECL|method|UpdateResult ()
specifier|public
name|UpdateResult
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
DECL|method|getUpdates ()
specifier|public
name|List
argument_list|<
name|Update
argument_list|>
name|getUpdates
parameter_list|()
block|{
return|return
name|updates
return|;
block|}
DECL|method|setUpdates (List<Update> updates)
specifier|public
name|void
name|setUpdates
parameter_list|(
name|List
argument_list|<
name|Update
argument_list|>
name|updates
parameter_list|)
block|{
name|this
operator|.
name|updates
operator|=
name|updates
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
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"UpdateResult{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"ok="
argument_list|)
operator|.
name|append
argument_list|(
name|ok
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", updates="
argument_list|)
operator|.
name|append
argument_list|(
name|updates
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

