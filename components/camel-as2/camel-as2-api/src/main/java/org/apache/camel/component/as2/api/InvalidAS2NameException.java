begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
package|;
end_package

begin_comment
comment|/**  * Thrown to indicated a given AS2 name is invalid.  */
end_comment

begin_class
DECL|class|InvalidAS2NameException
specifier|public
class|class
name|InvalidAS2NameException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6284079291785073089L
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|int
name|index
decl_stmt|;
comment|/**      * Constructs an<code>InvalidAS2NameException</code> for the      * specified name and index.      *      * @param name - the AS2 name that is invalid.      * @param index - the index in the<code>name</code> of the invalid character      */
DECL|method|InvalidAS2NameException (String name, int index)
specifier|public
name|InvalidAS2NameException
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see java.lang.Throwable#getMessage()      */
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
name|char
name|character
init|=
name|name
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|String
name|invalidChar
init|=
literal|""
operator|+
name|character
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isISOControl
argument_list|(
name|character
argument_list|)
condition|)
block|{
name|invalidChar
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"\\u%04x"
argument_list|,
operator|(
name|int
operator|)
name|character
argument_list|)
expr_stmt|;
block|}
return|return
literal|"Invalid character '"
operator|+
name|invalidChar
operator|+
literal|"' at index "
operator|+
name|index
return|;
block|}
comment|/**      * Returns the invalid AS2 name      *      * @return the invalid AS2 name      */
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
comment|/**      * Returns the index of the invalid character in<code>name</code>      *      * @return the index of the invalid character in<code>name</code>      */
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
block|}
end_class

end_unit

