begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
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
name|function
operator|.
name|Consumer
import|;
end_import

begin_comment
comment|/**  * Iterator used for SQL IN query.  *<p/>  * This ensures we know the parameters is an IN parameter and the values are dynamic and must be  * set using this iterator.  */
end_comment

begin_class
DECL|class|SqlInIterator
specifier|public
class|class
name|SqlInIterator
implements|implements
name|Iterator
block|{
DECL|field|it
specifier|private
specifier|final
name|Iterator
name|it
decl_stmt|;
DECL|method|SqlInIterator (Iterator it)
specifier|public
name|SqlInIterator
parameter_list|(
name|Iterator
name|it
parameter_list|)
block|{
name|this
operator|.
name|it
operator|=
name|it
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|it
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|Object
name|next
parameter_list|()
block|{
return|return
name|it
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
comment|// This method should not have @Override as its a new method in Java 1.8
comment|// and we need to compile for Java 1.7 also. TODO: enable again in Camel 2.18 onwards
comment|// @Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|forEachRemaining (Consumer action)
specifier|public
name|void
name|forEachRemaining
parameter_list|(
name|Consumer
name|action
parameter_list|)
block|{
name|it
operator|.
name|forEachRemaining
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

