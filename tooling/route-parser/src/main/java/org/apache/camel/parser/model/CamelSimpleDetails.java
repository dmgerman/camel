begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|model
package|;
end_package

begin_class
DECL|class|CamelSimpleDetails
specifier|public
class|class
name|CamelSimpleDetails
block|{
DECL|field|fileName
specifier|private
name|String
name|fileName
decl_stmt|;
DECL|field|lineNumber
specifier|private
name|String
name|lineNumber
decl_stmt|;
DECL|field|lineNumberEnd
specifier|private
name|String
name|lineNumberEnd
decl_stmt|;
DECL|field|className
specifier|private
name|String
name|className
decl_stmt|;
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
DECL|field|simple
specifier|private
name|String
name|simple
decl_stmt|;
DECL|field|predicate
specifier|private
name|boolean
name|predicate
decl_stmt|;
DECL|field|expression
specifier|private
name|boolean
name|expression
decl_stmt|;
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
DECL|method|setFileName (String fileName)
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
block|}
DECL|method|getLineNumber ()
specifier|public
name|String
name|getLineNumber
parameter_list|()
block|{
return|return
name|lineNumber
return|;
block|}
DECL|method|setLineNumber (String lineNumber)
specifier|public
name|void
name|setLineNumber
parameter_list|(
name|String
name|lineNumber
parameter_list|)
block|{
name|this
operator|.
name|lineNumber
operator|=
name|lineNumber
expr_stmt|;
block|}
DECL|method|getLineNumberEnd ()
specifier|public
name|String
name|getLineNumberEnd
parameter_list|()
block|{
return|return
name|lineNumberEnd
return|;
block|}
DECL|method|setLineNumberEnd (String lineNumberEnd)
specifier|public
name|void
name|setLineNumberEnd
parameter_list|(
name|String
name|lineNumberEnd
parameter_list|)
block|{
name|this
operator|.
name|lineNumberEnd
operator|=
name|lineNumberEnd
expr_stmt|;
block|}
DECL|method|getClassName ()
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
DECL|method|setClassName (String className)
specifier|public
name|void
name|setClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getSimple ()
specifier|public
name|String
name|getSimple
parameter_list|()
block|{
return|return
name|simple
return|;
block|}
DECL|method|setSimple (String simple)
specifier|public
name|void
name|setSimple
parameter_list|(
name|String
name|simple
parameter_list|)
block|{
name|this
operator|.
name|simple
operator|=
name|simple
expr_stmt|;
block|}
DECL|method|isPredicate ()
specifier|public
name|boolean
name|isPredicate
parameter_list|()
block|{
return|return
name|predicate
return|;
block|}
DECL|method|setPredicate (boolean predicate)
specifier|public
name|void
name|setPredicate
parameter_list|(
name|boolean
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
DECL|method|isExpression ()
specifier|public
name|boolean
name|isExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|setExpression (boolean expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|boolean
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
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
name|simple
return|;
block|}
block|}
end_class

end_unit

