begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|DataFormat
import|;
end_import

begin_class
DECL|class|BindyAbstractDataFormat
specifier|public
specifier|abstract
class|class
name|BindyAbstractDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|locale
specifier|private
name|String
name|locale
decl_stmt|;
DECL|field|modelFactory
specifier|private
name|BindyAbstractFactory
name|modelFactory
decl_stmt|;
DECL|field|classType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|classType
decl_stmt|;
DECL|method|BindyAbstractDataFormat ()
specifier|public
name|BindyAbstractDataFormat
parameter_list|()
block|{     }
DECL|method|BindyAbstractDataFormat (Class<?> classType)
specifier|protected
name|BindyAbstractDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
name|this
operator|.
name|classType
operator|=
name|classType
expr_stmt|;
block|}
DECL|method|getClassType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getClassType
parameter_list|()
block|{
return|return
name|classType
return|;
block|}
DECL|method|setClassType (Class<?> classType)
specifier|public
name|void
name|setClassType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
name|this
operator|.
name|classType
operator|=
name|classType
expr_stmt|;
block|}
DECL|method|getLocale ()
specifier|public
name|String
name|getLocale
parameter_list|()
block|{
return|return
name|locale
return|;
block|}
DECL|method|setLocale (String locale)
specifier|public
name|void
name|setLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|this
operator|.
name|locale
operator|=
name|locale
expr_stmt|;
block|}
DECL|method|getFactory ()
specifier|public
name|BindyAbstractFactory
name|getFactory
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|modelFactory
operator|==
literal|null
condition|)
block|{
name|modelFactory
operator|=
name|createModelFactory
argument_list|()
expr_stmt|;
name|modelFactory
operator|.
name|setLocale
argument_list|(
name|locale
argument_list|)
expr_stmt|;
block|}
return|return
name|modelFactory
return|;
block|}
DECL|method|setModelFactory (BindyAbstractFactory modelFactory)
specifier|public
name|void
name|setModelFactory
parameter_list|(
name|BindyAbstractFactory
name|modelFactory
parameter_list|)
block|{
name|this
operator|.
name|modelFactory
operator|=
name|modelFactory
expr_stmt|;
block|}
DECL|method|createModelFactory ()
specifier|protected
specifier|abstract
name|BindyAbstractFactory
name|createModelFactory
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|extractUnmarshalResult (List<Map<String, Object>> models)
specifier|protected
name|Object
name|extractUnmarshalResult
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
parameter_list|)
block|{
if|if
condition|(
name|getClassType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// we expect to find this type in the models, and grab only that type
name|List
argument_list|<
name|Object
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|models
control|)
block|{
name|Object
name|data
init|=
name|entry
operator|.
name|get
argument_list|(
name|getClassType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if there is only 1 then dont return a list
if|if
condition|(
name|answer
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
else|else
block|{
return|return
name|models
return|;
block|}
block|}
block|}
end_class

end_unit

