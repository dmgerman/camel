begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|osgi
package|;
end_package

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
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|util
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Version
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
DECL|field|META_INF_COMPONENT
specifier|public
specifier|static
specifier|final
name|String
name|META_INF_COMPONENT
init|=
literal|"META-INF/services/org/apache/camel/component/"
decl_stmt|;
DECL|field|META_INF_LANGUAGE
specifier|public
specifier|static
specifier|final
name|String
name|META_INF_LANGUAGE
init|=
literal|"META-INF/services/org/apache/camel/language/"
decl_stmt|;
DECL|field|META_INF_LANGUAGE_RESOLVER
specifier|public
specifier|static
specifier|final
name|String
name|META_INF_LANGUAGE_RESOLVER
init|=
literal|"META-INF/services/org/apache/camel/language/resolver/"
decl_stmt|;
DECL|field|META_INF_DATAFORMAT
specifier|public
specifier|static
specifier|final
name|String
name|META_INF_DATAFORMAT
init|=
literal|"META-INF/services/org/apache/camel/dataformat/"
decl_stmt|;
DECL|class|ListEnumeration
specifier|private
specifier|static
class|class
name|ListEnumeration
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Enumeration
argument_list|<
name|E
argument_list|>
block|{
DECL|field|list
specifier|private
specifier|final
name|List
argument_list|<
name|E
argument_list|>
name|list
decl_stmt|;
DECL|field|index
specifier|private
name|int
name|index
decl_stmt|;
DECL|method|ListEnumeration (List<E> list)
name|ListEnumeration
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
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
operator|!=
literal|null
operator|&&
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
name|E
name|nextElement
parameter_list|()
block|{
name|E
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
DECL|method|CamelMockBundle ()
specifier|public
name|CamelMockBundle
parameter_list|()
block|{
name|setClassLoader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getListEnumeration (String prefix, String entrys[])
specifier|private
name|Enumeration
argument_list|<
name|String
argument_list|>
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
argument_list|<
name|String
argument_list|>
argument_list|(
name|list
argument_list|)
return|;
block|}
DECL|method|getEntryPaths (String path)
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getEntryPaths
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
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
name|META_INF_COMPONENT
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
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
name|META_INF_LANGUAGE
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|META_INF_LANGUAGE_RESOLVER
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
literal|"default"
block|}
decl_stmt|;
name|result
operator|=
name|getListEnumeration
argument_list|(
name|META_INF_LANGUAGE_RESOLVER
argument_list|,
name|entries
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|findEntries (String path, String filePattern, boolean recurse)
specifier|public
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|findEntries
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|filePattern
parameter_list|,
name|boolean
name|recurse
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|equals
argument_list|(
literal|"/org/apache/camel/core/osgi/test"
argument_list|)
operator|&&
name|filePattern
operator|.
name|equals
argument_list|(
literal|"*.class"
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/camel/core/osgi/test/MyTypeConverter.class"
argument_list|)
decl_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|url
operator|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"org/apache/camel/core/osgi/test/MyRouteBuilder.class"
argument_list|)
expr_stmt|;
name|urls
operator|.
name|add
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
operator|new
name|ListEnumeration
argument_list|<
name|URL
argument_list|>
argument_list|(
name|urls
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|super
operator|.
name|findEntries
argument_list|(
name|path
argument_list|,
name|filePattern
argument_list|,
name|recurse
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSignerCertificates (int signersType)
specifier|public
name|Map
argument_list|<
name|X509Certificate
argument_list|,
name|List
argument_list|<
name|X509Certificate
argument_list|>
argument_list|>
name|getSignerCertificates
parameter_list|(
name|int
name|signersType
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getVersion ()
specifier|public
name|Version
name|getVersion
parameter_list|()
block|{
return|return
name|Version
operator|.
name|parseVersion
argument_list|(
literal|"1.0.0"
argument_list|)
return|;
block|}
DECL|method|loadClass (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|isLoadableClass
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|loadClass
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
block|}
DECL|method|isLoadableClass (String name)
specifier|protected
name|boolean
name|isLoadableClass
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|!
name|name
operator|.
name|startsWith
argument_list|(
literal|"org.apache.camel.core.osgi.other"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

