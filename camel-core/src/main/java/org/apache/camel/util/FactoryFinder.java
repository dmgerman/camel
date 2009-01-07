begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|io
operator|.
name|InputStream
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
name|Collections
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|Injector
import|;
end_import

begin_comment
comment|/**  * Finder to find factories from the resource classpath, usually<b>META-INF/services/org/apache/camel/</b>.  */
end_comment

begin_class
DECL|class|FactoryFinder
specifier|public
class|class
name|FactoryFinder
block|{
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|classMap
specifier|protected
specifier|final
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|classMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|FactoryFinder ()
specifier|public
name|FactoryFinder
parameter_list|()
block|{
name|this
argument_list|(
literal|"META-INF/services/org/apache/camel/"
argument_list|)
expr_stmt|;
block|}
DECL|method|FactoryFinder (String path)
specifier|public
name|FactoryFinder
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
comment|/**      * Creates a new instance of the given key      *      * @param key is the key to add to the path to find a text file containing      *            the factory name      * @return a newly created instance      */
DECL|method|newInstance (String key)
specifier|public
name|Object
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InstantiationException
throws|,
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|newInstance
argument_list|(
name|key
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
return|;
block|}
DECL|method|newInstance (String key, String propertyPrefix)
specifier|public
name|Object
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|propertyPrefix
parameter_list|)
throws|throws
name|IllegalAccessException
throws|,
name|InstantiationException
throws|,
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|Class
name|clazz
init|=
name|findClass
argument_list|(
name|key
argument_list|,
name|propertyPrefix
argument_list|)
decl_stmt|;
return|return
name|clazz
operator|.
name|newInstance
argument_list|()
return|;
block|}
DECL|method|newInstance (String key, Injector injector)
specifier|public
name|Object
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|,
name|Injector
name|injector
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|newInstance
argument_list|(
name|key
argument_list|,
name|injector
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
return|;
block|}
DECL|method|newInstance (String key, Injector injector, String propertyPrefix)
specifier|public
name|Object
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|,
name|Injector
name|injector
parameter_list|,
name|String
name|propertyPrefix
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|findClass
argument_list|(
name|key
argument_list|,
name|propertyPrefix
argument_list|)
decl_stmt|;
return|return
name|injector
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|newInstance (String key, Injector injector, Class<T> expectedType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|,
name|Injector
name|injector
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|expectedType
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
return|return
name|newInstance
argument_list|(
name|key
argument_list|,
name|injector
argument_list|,
literal|null
argument_list|,
name|expectedType
argument_list|)
return|;
block|}
DECL|method|newInstance (String key, Injector injector, String propertyPrefix, Class<T> expectedType)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|String
name|key
parameter_list|,
name|Injector
name|injector
parameter_list|,
name|String
name|propertyPrefix
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|expectedType
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|findClass
argument_list|(
name|key
argument_list|,
name|propertyPrefix
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|injector
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedType
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|expectedType
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ClassCastException
argument_list|(
literal|"Not instanceof "
operator|+
name|expectedType
operator|.
name|getName
argument_list|()
operator|+
literal|" value: "
operator|+
name|value
argument_list|)
throw|;
block|}
block|}
DECL|method|newInstances (String key, Injector injector, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|newInstances
parameter_list|(
name|String
name|key
parameter_list|,
name|Injector
name|injector
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|List
argument_list|<
name|Class
argument_list|>
name|list
init|=
name|findClasses
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|T
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|newInstance
argument_list|(
name|key
argument_list|,
name|injector
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|findClass (String key)
specifier|public
name|Class
name|findClass
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
return|return
name|findClass
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|findClass (String key, String propertyPrefix)
specifier|public
name|Class
name|findClass
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|propertyPrefix
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
name|propertyPrefix
operator|==
literal|null
condition|)
block|{
name|propertyPrefix
operator|=
literal|""
expr_stmt|;
block|}
name|Class
name|clazz
init|=
operator|(
name|Class
operator|)
name|classMap
operator|.
name|get
argument_list|(
name|propertyPrefix
operator|+
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
name|clazz
operator|=
name|newInstance
argument_list|(
name|doFindFactoryProperties
argument_list|(
name|key
argument_list|)
argument_list|,
name|propertyPrefix
argument_list|)
expr_stmt|;
name|classMap
operator|.
name|put
argument_list|(
name|propertyPrefix
operator|+
name|key
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
return|return
name|clazz
return|;
block|}
DECL|method|findClasses (String key)
specifier|public
name|List
argument_list|<
name|Class
argument_list|>
name|findClasses
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
return|return
name|findClasses
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|findClasses (String key, String propertyPrefix)
specifier|public
name|List
argument_list|<
name|Class
argument_list|>
name|findClasses
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|propertyPrefix
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
comment|// TODO change to support finding multiple classes on the classpath!
name|Class
name|type
init|=
name|findClass
argument_list|(
name|key
argument_list|,
name|propertyPrefix
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|newInstance (Properties properties, String propertyPrefix)
specifier|private
name|Class
name|newInstance
parameter_list|(
name|Properties
name|properties
parameter_list|,
name|String
name|propertyPrefix
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
name|String
name|className
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|propertyPrefix
operator|+
literal|"class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected property is missing: "
operator|+
name|propertyPrefix
operator|+
literal|"class"
argument_list|)
throw|;
block|}
return|return
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
DECL|method|doFindFactoryProperties (String key)
specifier|private
name|Properties
name|doFindFactoryProperties
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|uri
init|=
name|path
operator|+
name|key
decl_stmt|;
name|InputStream
name|in
init|=
name|ObjectHelper
operator|.
name|loadResourceAsStream
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoFactoryAvailableException
argument_list|(
name|uri
argument_list|)
throw|;
block|}
comment|// lets load the file
name|BufferedInputStream
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reader
operator|=
operator|new
name|BufferedInputStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|reader
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|close
argument_list|(
name|in
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

