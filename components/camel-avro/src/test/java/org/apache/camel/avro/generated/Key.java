begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|// CHECKSTYLE:OFF
end_comment

begin_comment
comment|/**  * Autogenerated by Avro  *   * DO NOT EDIT DIRECTLY  */
end_comment

begin_package
DECL|package|org.apache.camel.avro.generated
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
package|;
end_package

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
DECL|class|Key
specifier|public
class|class
name|Key
extends|extends
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|specific
operator|.
name|SpecificRecordBase
implements|implements
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|specific
operator|.
name|SpecificRecord
block|{
DECL|field|SCHEMA$
specifier|public
specifier|static
specifier|final
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Schema
name|SCHEMA$
init|=
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Schema
operator|.
name|parse
argument_list|(
literal|"{\"type\":\"record\",\"name\":\"Key\",\"namespace\":\"org.apache.camel.avro.generated\",\"fields\":[{\"name\":\"key\",\"type\":\"string\"}]}"
argument_list|)
decl_stmt|;
DECL|field|key
annotation|@
name|Deprecated
specifier|public
name|java
operator|.
name|lang
operator|.
name|CharSequence
name|key
decl_stmt|;
DECL|method|getSchema ()
specifier|public
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Schema
name|getSchema
parameter_list|()
block|{
return|return
name|SCHEMA$
return|;
block|}
comment|// Used by DatumWriter.  Applications should not call.
DECL|method|get (int field$)
specifier|public
name|java
operator|.
name|lang
operator|.
name|Object
name|get
parameter_list|(
name|int
name|field$
parameter_list|)
block|{
switch|switch
condition|(
name|field$
condition|)
block|{
case|case
literal|0
case|:
return|return
name|key
return|;
default|default:
throw|throw
operator|new
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|AvroRuntimeException
argument_list|(
literal|"Bad index"
argument_list|)
throw|;
block|}
block|}
comment|// Used by DatumReader.  Applications should not call.
annotation|@
name|SuppressWarnings
argument_list|(
name|value
operator|=
literal|"unchecked"
argument_list|)
DECL|method|put (int field$, java.lang.Object value$)
specifier|public
name|void
name|put
parameter_list|(
name|int
name|field$
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|Object
name|value$
parameter_list|)
block|{
switch|switch
condition|(
name|field$
condition|)
block|{
case|case
literal|0
case|:
name|key
operator|=
operator|(
name|java
operator|.
name|lang
operator|.
name|CharSequence
operator|)
name|value$
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|AvroRuntimeException
argument_list|(
literal|"Bad index"
argument_list|)
throw|;
block|}
block|}
comment|/**    * Gets the value of the 'key' field.    */
DECL|method|getKey ()
specifier|public
name|java
operator|.
name|lang
operator|.
name|CharSequence
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**    * Sets the value of the 'key' field.    * @param value the value to set.    */
DECL|method|setKey (java.lang.CharSequence value)
specifier|public
name|void
name|setKey
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|CharSequence
name|value
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|value
expr_stmt|;
block|}
comment|/** Creates a new Key RecordBuilder */
DECL|method|newBuilder ()
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
name|newBuilder
parameter_list|()
block|{
return|return
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
argument_list|()
return|;
block|}
comment|/** Creates a new Key RecordBuilder by copying an existing Builder */
DECL|method|newBuilder (org.apache.camel.avro.generated.Key.Builder other)
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
name|newBuilder
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
name|other
parameter_list|)
block|{
return|return
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
argument_list|(
name|other
argument_list|)
return|;
block|}
comment|/** Creates a new Key RecordBuilder by copying an existing Key instance */
DECL|method|newBuilder (org.apache.camel.avro.generated.Key other)
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
name|newBuilder
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
name|other
parameter_list|)
block|{
return|return
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
argument_list|(
name|other
argument_list|)
return|;
block|}
comment|/**    * RecordBuilder for Key instances.    */
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
extends|extends
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|specific
operator|.
name|SpecificRecordBuilderBase
argument_list|<
name|Key
argument_list|>
implements|implements
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|data
operator|.
name|RecordBuilder
argument_list|<
name|Key
argument_list|>
block|{
DECL|field|key
specifier|private
name|java
operator|.
name|lang
operator|.
name|CharSequence
name|key
decl_stmt|;
comment|/** Creates a new Builder */
DECL|method|Builder ()
specifier|private
name|Builder
parameter_list|()
block|{
name|super
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|SCHEMA$
argument_list|)
expr_stmt|;
block|}
comment|/** Creates a Builder by copying an existing Builder */
DECL|method|Builder (org.apache.camel.avro.generated.Key.Builder other)
specifier|private
name|Builder
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
name|other
parameter_list|)
block|{
name|super
argument_list|(
name|other
argument_list|)
expr_stmt|;
block|}
comment|/** Creates a Builder by copying an existing Key instance */
DECL|method|Builder (org.apache.camel.avro.generated.Key other)
specifier|private
name|Builder
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
name|other
parameter_list|)
block|{
name|super
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|SCHEMA$
argument_list|)
expr_stmt|;
if|if
condition|(
name|isValidValue
argument_list|(
name|fields
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|other
operator|.
name|key
argument_list|)
condition|)
block|{
name|this
operator|.
name|key
operator|=
name|data
argument_list|()
operator|.
name|deepCopy
argument_list|(
name|fields
argument_list|()
index|[
literal|0
index|]
operator|.
name|schema
argument_list|()
argument_list|,
name|other
operator|.
name|key
argument_list|)
expr_stmt|;
name|fieldSetFlags
argument_list|()
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/** Gets the value of the 'key' field */
DECL|method|getKey ()
specifier|public
name|java
operator|.
name|lang
operator|.
name|CharSequence
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/** Sets the value of the 'key' field */
DECL|method|setKey (java.lang.CharSequence value)
specifier|public
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
name|setKey
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|CharSequence
name|value
parameter_list|)
block|{
name|validate
argument_list|(
name|fields
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|value
expr_stmt|;
name|fieldSetFlags
argument_list|()
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Checks whether the 'key' field has been set */
DECL|method|hasKey ()
specifier|public
name|boolean
name|hasKey
parameter_list|()
block|{
return|return
name|fieldSetFlags
argument_list|()
index|[
literal|0
index|]
return|;
block|}
comment|/** Clears the value of the 'key' field */
DECL|method|clearKey ()
specifier|public
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|avro
operator|.
name|generated
operator|.
name|Key
operator|.
name|Builder
name|clearKey
parameter_list|()
block|{
name|key
operator|=
literal|null
expr_stmt|;
name|fieldSetFlags
argument_list|()
index|[
literal|0
index|]
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|build ()
specifier|public
name|Key
name|build
parameter_list|()
block|{
try|try
block|{
name|Key
name|record
init|=
operator|new
name|Key
argument_list|()
decl_stmt|;
name|record
operator|.
name|key
operator|=
name|fieldSetFlags
argument_list|()
index|[
literal|0
index|]
condition|?
name|this
operator|.
name|key
else|:
operator|(
name|java
operator|.
name|lang
operator|.
name|CharSequence
operator|)
name|defaultValue
argument_list|(
name|fields
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
return|return
name|record
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|AvroRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

