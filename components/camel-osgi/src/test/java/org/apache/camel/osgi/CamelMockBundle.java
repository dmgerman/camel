begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|Enumeration
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
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|mock
operator|.
name|MockBundle
import|;
end_import

begin_comment
comment|/**  *  The mock bundle will make up a normal camel-components bundle  */
end_comment

begin_class
DECL|class|CamelMockBundle
specifier|public
class|class
name|CamelMockBundle
extends|extends
name|MockBundle
block|{
DECL|class|ListEnumeration
specifier|private
class|class
name|ListEnumeration
implements|implements
name|Enumeration
block|{
DECL|field|list
specifier|private
specifier|final
name|List
name|list
decl_stmt|;
DECL|field|index
specifier|private
name|int
name|index
decl_stmt|;
DECL|method|ListEnumeration (List list)
specifier|public
name|ListEnumeration
parameter_list|(
name|List
name|list
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
DECL|method|hasMoreElements ()
specifier|public
name|boolean
name|hasMoreElements
parameter_list|()
block|{
return|return
name|list
operator|==
literal|null
condition|?
literal|false
else|:
name|index
operator|<
name|list
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|nextElement ()
specifier|public
name|Object
name|nextElement
parameter_list|()
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|list
operator|.
name|get
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
DECL|method|getListEnumeration (String prefix, String entrys[])
specifier|private
name|Enumeration
name|getListEnumeration
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|entrys
index|[]
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|entry
range|:
name|entrys
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|prefix
operator|+
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ListEnumeration
argument_list|(
name|list
argument_list|)
return|;
block|}
DECL|method|getEntryPaths (String path)
specifier|public
name|Enumeration
name|getEntryPaths
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|Enumeration
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Activator
operator|.
name|META_INF_COMPONENT
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|String
index|[]
name|entries
init|=
operator|new
name|String
index|[]
block|{
literal|"timer_test"
block|,
literal|"file_test"
block|}
decl_stmt|;
name|result
operator|=
name|getListEnumeration
argument_list|(
name|Activator
operator|.
name|META_INF_COMPONENT
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Activator
operator|.
name|META_INF_LANGUAGE
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|String
index|[]
name|entries
init|=
operator|new
name|String
index|[]
block|{
literal|"bean_test"
block|,
literal|"file_test"
block|}
decl_stmt|;
name|result
operator|=
name|getListEnumeration
argument_list|(
name|Activator
operator|.
name|META_INF_LANGUAGE
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

