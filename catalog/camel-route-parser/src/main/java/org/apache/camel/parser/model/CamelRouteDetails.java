begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Details about a parsed and discovered Camel route.  */
end_comment

begin_class
DECL|class|CamelRouteDetails
specifier|public
class|class
name|CamelRouteDetails
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
DECL|field|routeId
specifier|private
name|String
name|routeId
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
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
DECL|method|setRouteId (String routeId)
specifier|public
name|void
name|setRouteId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|CamelRouteDetails
name|that
init|=
operator|(
name|CamelRouteDetails
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|fileName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|fileName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|lineNumber
operator|!=
literal|null
condition|?
operator|!
name|lineNumber
operator|.
name|equals
argument_list|(
name|that
operator|.
name|lineNumber
argument_list|)
else|:
name|that
operator|.
name|lineNumber
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|lineNumberEnd
operator|!=
literal|null
condition|?
operator|!
name|lineNumberEnd
operator|.
name|equals
argument_list|(
name|that
operator|.
name|lineNumberEnd
argument_list|)
else|:
name|that
operator|.
name|lineNumberEnd
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|className
operator|!=
literal|null
condition|?
operator|!
name|className
operator|.
name|equals
argument_list|(
name|that
operator|.
name|className
argument_list|)
else|:
name|that
operator|.
name|className
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|methodName
operator|!=
literal|null
condition|?
operator|!
name|methodName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|methodName
argument_list|)
else|:
name|that
operator|.
name|methodName
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|routeId
operator|!=
literal|null
condition|?
operator|!
name|routeId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|routeId
argument_list|)
else|:
name|that
operator|.
name|routeId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|fileName
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|lineNumber
operator|!=
literal|null
condition|?
name|lineNumber
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|lineNumberEnd
operator|!=
literal|null
condition|?
name|lineNumberEnd
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|className
operator|!=
literal|null
condition|?
name|className
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|methodName
operator|!=
literal|null
condition|?
name|methodName
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|routeId
operator|!=
literal|null
condition|?
name|routeId
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
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
literal|"CamelRouteDetails["
operator|+
literal|"fileName='"
operator|+
name|fileName
operator|+
literal|'\''
operator|+
literal|", lineNumber='"
operator|+
name|lineNumber
operator|+
literal|'\''
operator|+
literal|", lineNumberEnd='"
operator|+
name|lineNumberEnd
operator|+
literal|'\''
operator|+
literal|", className='"
operator|+
name|className
operator|+
literal|'\''
operator|+
literal|", methodName='"
operator|+
name|methodName
operator|+
literal|'\''
operator|+
literal|", routeId='"
operator|+
name|routeId
operator|+
literal|'\''
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

