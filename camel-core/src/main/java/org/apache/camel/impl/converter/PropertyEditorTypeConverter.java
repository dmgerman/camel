begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyEditor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyEditorManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Exchange
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
name|TypeConverter
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
name|LRUSoftCache
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Uses the {@link java.beans.PropertyEditor} conversion system to convert Objects to  * and from String values.  *  * @version   */
end_comment

begin_class
DECL|class|PropertyEditorTypeConverter
specifier|public
class|class
name|PropertyEditorTypeConverter
implements|implements
name|TypeConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PropertyEditorTypeConverter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// use a soft bound cache to avoid using too much memory in case a lot of different classes
comment|// is being converted to string
DECL|field|misses
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|misses
init|=
operator|new
name|LRUSoftCache
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
comment|// we don't anticipate so many property editors so we have unbounded map
DECL|field|cache
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|PropertyEditor
argument_list|>
name|cache
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|PropertyEditor
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|convertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// We can't convert null values since we can't figure out a property
comment|// editor for it.
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|==
name|String
operator|.
name|class
condition|)
block|{
comment|// No conversion needed.
if|if
condition|(
name|type
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|key
init|=
name|type
decl_stmt|;
name|PropertyEditor
name|editor
init|=
name|lookupEditor
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|editor
operator|!=
literal|null
condition|)
block|{
name|editor
operator|.
name|setAsText
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|type
argument_list|,
name|editor
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|key
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|PropertyEditor
name|editor
init|=
name|lookupEditor
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|editor
operator|!=
literal|null
condition|)
block|{
name|editor
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|type
argument_list|,
name|editor
operator|.
name|getAsText
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|lookupEditor (Class<?> type)
specifier|private
name|PropertyEditor
name|lookupEditor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
comment|// check misses first
if|if
condition|(
name|misses
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No previously found property editor for type: {}"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
synchronized|synchronized
init|(
name|cache
init|)
block|{
comment|// not a miss then try to lookup the editor
name|PropertyEditor
name|editor
init|=
name|cache
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|editor
operator|==
literal|null
condition|)
block|{
comment|// findEditor is synchronized and very slow so we want to only lookup once for a given key
comment|// and then we use our own local cache for faster lookup
name|editor
operator|=
name|PropertyEditorManager
operator|.
name|findEditor
argument_list|(
name|type
argument_list|)
expr_stmt|;
comment|// either we found an editor, or if not then register it as a miss
if|if
condition|(
name|editor
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found property editor for type: {} -> {}"
argument_list|,
name|type
argument_list|,
name|editor
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|editor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot find property editor for type: {}"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|misses
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|editor
return|;
block|}
block|}
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

