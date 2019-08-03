begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|MaskingFormatter
import|;
end_import

begin_comment
comment|/**  * The {@link MaskingFormatter} that searches the specified keywords in the source  * and replace its value with mask string. By default passphrase, password and secretKey  * are used as keywords to replace its value.  */
end_comment

begin_class
DECL|class|DefaultMaskingFormatter
specifier|public
class|class
name|DefaultMaskingFormatter
implements|implements
name|MaskingFormatter
block|{
DECL|field|DEFAULT_KEYWORDS
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|DEFAULT_KEYWORDS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"passphrase"
argument_list|,
literal|"password"
argument_list|,
literal|"secretKey"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|keywords
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|keywords
decl_stmt|;
DECL|field|maskKeyValue
specifier|private
name|boolean
name|maskKeyValue
decl_stmt|;
DECL|field|maskXmlElement
specifier|private
name|boolean
name|maskXmlElement
decl_stmt|;
DECL|field|maskJson
specifier|private
name|boolean
name|maskJson
decl_stmt|;
DECL|field|maskString
specifier|private
name|String
name|maskString
init|=
literal|"xxxxx"
decl_stmt|;
DECL|field|keyValueMaskPattern
specifier|private
name|Pattern
name|keyValueMaskPattern
decl_stmt|;
DECL|field|xmlElementMaskPattern
specifier|private
name|Pattern
name|xmlElementMaskPattern
decl_stmt|;
DECL|field|jsonMaskPattern
specifier|private
name|Pattern
name|jsonMaskPattern
decl_stmt|;
DECL|method|DefaultMaskingFormatter ()
specifier|public
name|DefaultMaskingFormatter
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_KEYWORDS
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultMaskingFormatter (boolean maskKeyValue, boolean maskXml, boolean maskJson)
specifier|public
name|DefaultMaskingFormatter
parameter_list|(
name|boolean
name|maskKeyValue
parameter_list|,
name|boolean
name|maskXml
parameter_list|,
name|boolean
name|maskJson
parameter_list|)
block|{
name|this
argument_list|(
name|DEFAULT_KEYWORDS
argument_list|,
name|maskKeyValue
argument_list|,
name|maskXml
argument_list|,
name|maskJson
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultMaskingFormatter (Set<String> keywords, boolean maskKeyValue, boolean maskXmlElement, boolean maskJson)
specifier|public
name|DefaultMaskingFormatter
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|keywords
parameter_list|,
name|boolean
name|maskKeyValue
parameter_list|,
name|boolean
name|maskXmlElement
parameter_list|,
name|boolean
name|maskJson
parameter_list|)
block|{
name|this
operator|.
name|keywords
operator|=
name|keywords
expr_stmt|;
name|setMaskKeyValue
argument_list|(
name|maskKeyValue
argument_list|)
expr_stmt|;
name|setMaskXmlElement
argument_list|(
name|maskXmlElement
argument_list|)
expr_stmt|;
name|setMaskJson
argument_list|(
name|maskJson
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|format (String source)
specifier|public
name|String
name|format
parameter_list|(
name|String
name|source
parameter_list|)
block|{
if|if
condition|(
name|keywords
operator|==
literal|null
operator|||
name|keywords
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|source
return|;
block|}
name|String
name|answer
init|=
name|source
decl_stmt|;
if|if
condition|(
name|maskKeyValue
condition|)
block|{
name|answer
operator|=
name|keyValueMaskPattern
operator|.
name|matcher
argument_list|(
name|answer
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"$1\""
operator|+
name|maskString
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maskXmlElement
condition|)
block|{
name|answer
operator|=
name|xmlElementMaskPattern
operator|.
name|matcher
argument_list|(
name|answer
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"$1"
operator|+
name|maskString
operator|+
literal|"$3"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maskJson
condition|)
block|{
name|answer
operator|=
name|jsonMaskPattern
operator|.
name|matcher
argument_list|(
name|answer
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"$1\""
operator|+
name|maskString
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|isMaskKeyValue ()
specifier|public
name|boolean
name|isMaskKeyValue
parameter_list|()
block|{
return|return
name|maskKeyValue
return|;
block|}
DECL|method|setMaskKeyValue (boolean maskKeyValue)
specifier|public
name|void
name|setMaskKeyValue
parameter_list|(
name|boolean
name|maskKeyValue
parameter_list|)
block|{
name|this
operator|.
name|maskKeyValue
operator|=
name|maskKeyValue
expr_stmt|;
if|if
condition|(
name|maskKeyValue
condition|)
block|{
name|keyValueMaskPattern
operator|=
name|createKeyValueMaskPattern
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyValueMaskPattern
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|isMaskXmlElement ()
specifier|public
name|boolean
name|isMaskXmlElement
parameter_list|()
block|{
return|return
name|maskXmlElement
return|;
block|}
DECL|method|setMaskXmlElement (boolean maskXml)
specifier|public
name|void
name|setMaskXmlElement
parameter_list|(
name|boolean
name|maskXml
parameter_list|)
block|{
name|this
operator|.
name|maskXmlElement
operator|=
name|maskXml
expr_stmt|;
if|if
condition|(
name|maskXml
condition|)
block|{
name|xmlElementMaskPattern
operator|=
name|createXmlElementMaskPattern
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlElementMaskPattern
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|isMaskJson ()
specifier|public
name|boolean
name|isMaskJson
parameter_list|()
block|{
return|return
name|maskJson
return|;
block|}
DECL|method|setMaskJson (boolean maskJson)
specifier|public
name|void
name|setMaskJson
parameter_list|(
name|boolean
name|maskJson
parameter_list|)
block|{
name|this
operator|.
name|maskJson
operator|=
name|maskJson
expr_stmt|;
if|if
condition|(
name|maskJson
condition|)
block|{
name|jsonMaskPattern
operator|=
name|createJsonMaskPattern
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jsonMaskPattern
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getMaskString ()
specifier|public
name|String
name|getMaskString
parameter_list|()
block|{
return|return
name|maskString
return|;
block|}
DECL|method|setMaskString (String maskString)
specifier|public
name|void
name|setMaskString
parameter_list|(
name|String
name|maskString
parameter_list|)
block|{
name|this
operator|.
name|maskString
operator|=
name|maskString
expr_stmt|;
block|}
DECL|method|createKeyValueMaskPattern (Set<String> keywords)
specifier|protected
name|Pattern
name|createKeyValueMaskPattern
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|keywords
parameter_list|)
block|{
name|StringBuilder
name|regex
init|=
name|createOneOfThemRegex
argument_list|(
name|keywords
argument_list|)
decl_stmt|;
if|if
condition|(
name|regex
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|regex
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"([\\w]*(?:"
argument_list|)
expr_stmt|;
name|regex
operator|.
name|append
argument_list|(
literal|")[\\w]*[\\s]*?=[\\s]*?)([\\S&&[^'\",\\}\\]\\)]]+[\\S&&[^,\\}\\]\\)>]]*?|\"[^\"]*?\"|'[^']*?')"
argument_list|)
expr_stmt|;
return|return
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
operator|.
name|toString
argument_list|()
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
return|;
block|}
DECL|method|createXmlElementMaskPattern (Set<String> keywords)
specifier|protected
name|Pattern
name|createXmlElementMaskPattern
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|keywords
parameter_list|)
block|{
name|StringBuilder
name|regex
init|=
name|createOneOfThemRegex
argument_list|(
name|keywords
argument_list|)
decl_stmt|;
if|if
condition|(
name|regex
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|regex
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"(<([\\w]*(?:"
argument_list|)
expr_stmt|;
name|regex
operator|.
name|append
argument_list|(
literal|")[\\w]*)(?:[\\s]+.+)*?>[\\s]*?)(?:[\\S&&[^<]]+(?:\\s+[\\S&&[^<]]+)*?)([\\s]*?</\\2>)"
argument_list|)
expr_stmt|;
return|return
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
operator|.
name|toString
argument_list|()
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
return|;
block|}
DECL|method|createJsonMaskPattern (Set<String> keywords)
specifier|protected
name|Pattern
name|createJsonMaskPattern
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|keywords
parameter_list|)
block|{
name|StringBuilder
name|regex
init|=
name|createOneOfThemRegex
argument_list|(
name|keywords
argument_list|)
decl_stmt|;
if|if
condition|(
name|regex
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|regex
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"(\"(?:[^\"]|(?:\\\"))*?(?:"
argument_list|)
expr_stmt|;
name|regex
operator|.
name|append
argument_list|(
literal|")(?:[^\"]|(?:\\\"))*?\"\\s*?\\:\\s*?)(?:\"(?:[^\"]|(?:\\\"))*?\")"
argument_list|)
expr_stmt|;
return|return
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
operator|.
name|toString
argument_list|()
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
return|;
block|}
DECL|method|createOneOfThemRegex (Set<String> keywords)
specifier|protected
name|StringBuilder
name|createOneOfThemRegex
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|keywords
parameter_list|)
block|{
name|StringBuilder
name|regex
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|keywords
operator|==
literal|null
operator|||
name|keywords
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|strKeywords
init|=
name|keywords
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|regex
operator|.
name|append
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
name|strKeywords
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|strKeywords
operator|.
name|length
operator|>
literal|1
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|strKeywords
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|regex
operator|.
name|append
argument_list|(
literal|'|'
argument_list|)
expr_stmt|;
name|regex
operator|.
name|append
argument_list|(
name|Pattern
operator|.
name|quote
argument_list|(
name|strKeywords
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|regex
return|;
block|}
block|}
end_class

end_unit

