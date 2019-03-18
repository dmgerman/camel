begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
package|;
end_package

begin_comment
comment|/**  * Result of parsing Camel RouteBuilder or XML routes from the source code.  */
end_comment

begin_class
DECL|class|ParserResult
specifier|public
class|class
name|ParserResult
block|{
DECL|field|node
specifier|private
specifier|final
name|String
name|node
decl_stmt|;
DECL|field|parsed
specifier|private
name|boolean
name|parsed
decl_stmt|;
DECL|field|position
specifier|private
name|int
name|position
decl_stmt|;
DECL|field|length
specifier|private
name|int
name|length
decl_stmt|;
DECL|field|element
specifier|private
name|String
name|element
decl_stmt|;
DECL|field|predicate
specifier|private
name|Boolean
name|predicate
decl_stmt|;
DECL|method|ParserResult (String node, int position, int length, String element)
specifier|public
name|ParserResult
parameter_list|(
name|String
name|node
parameter_list|,
name|int
name|position
parameter_list|,
name|int
name|length
parameter_list|,
name|String
name|element
parameter_list|)
block|{
name|this
argument_list|(
name|node
argument_list|,
name|position
argument_list|,
name|length
argument_list|,
name|element
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|ParserResult (String node, int position, int length, String element, boolean parsed)
specifier|public
name|ParserResult
parameter_list|(
name|String
name|node
parameter_list|,
name|int
name|position
parameter_list|,
name|int
name|length
parameter_list|,
name|String
name|element
parameter_list|,
name|boolean
name|parsed
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|position
operator|=
name|position
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|length
expr_stmt|;
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
name|this
operator|.
name|parsed
operator|=
name|parsed
expr_stmt|;
block|}
comment|/**      * Character based position in the source code (not line based).      */
DECL|method|getPosition ()
specifier|public
name|int
name|getPosition
parameter_list|()
block|{
return|return
name|position
return|;
block|}
comment|/**      * Length of node in the source code (not line based).      */
DECL|method|getLength ()
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|length
return|;
block|}
comment|/**      * The element such as a Camel endpoint uri      */
DECL|method|getElement ()
specifier|public
name|String
name|getElement
parameter_list|()
block|{
return|return
name|element
return|;
block|}
comment|/**      * Whether the element was successfully parsed. If the parser cannot parse      * the element for whatever reason this will return<tt>false</tt>.      */
DECL|method|isParsed ()
specifier|public
name|boolean
name|isParsed
parameter_list|()
block|{
return|return
name|parsed
return|;
block|}
comment|/**      * The node which is typically a Camel EIP name such as<tt>to</tt>,<tt>wireTap</tt> etc.      */
DECL|method|getNode ()
specifier|public
name|String
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
DECL|method|getPredicate ()
specifier|public
name|Boolean
name|getPredicate
parameter_list|()
block|{
return|return
name|predicate
return|;
block|}
comment|/**      * Tells if it was an expression which is intended to be used as a predicate (determined from camel-core mode)      */
DECL|method|setPredicate (Boolean predicate)
specifier|public
name|void
name|setPredicate
parameter_list|(
name|Boolean
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|element
return|;
block|}
block|}
end_class

end_unit

