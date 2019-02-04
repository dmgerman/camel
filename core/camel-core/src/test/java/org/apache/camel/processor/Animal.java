begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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

begin_comment
comment|/**  *  */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_class
DECL|class|Animal
specifier|public
class|class
name|Animal
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
literal|1L
decl_stmt|;
DECL|field|id
specifier|private
name|int
name|id
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|Animal ()
specifier|public
name|Animal
parameter_list|()
block|{     }
DECL|method|Animal (int id, String name)
specifier|public
name|Animal
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|deepClone ()
specifier|public
name|Animal
name|deepClone
parameter_list|()
block|{
name|Animal
name|clone
init|=
operator|new
name|Animal
argument_list|()
decl_stmt|;
name|clone
operator|.
name|setId
argument_list|(
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setName
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|clone
return|;
block|}
DECL|method|getId ()
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (int id)
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
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
name|id
operator|+
literal|" "
operator|+
name|name
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

