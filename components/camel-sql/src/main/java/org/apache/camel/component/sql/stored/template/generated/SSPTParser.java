begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JavaCC: Do not edit this line. SSPTParser.java */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template.generated
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|generated
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
operator|.
name|*
import|;
end_import

begin_class
DECL|class|SSPTParser
specifier|public
class|class
name|SSPTParser
implements|implements
name|SSPTParserConstants
block|{
DECL|field|paramaterNameCounter
name|int
name|paramaterNameCounter
init|=
literal|0
decl_stmt|;
DECL|method|createNextParameterName ()
name|String
name|createNextParameterName
parameter_list|()
block|{
return|return
literal|"_"
operator|+
operator|(
name|paramaterNameCounter
operator|++
operator|)
return|;
block|}
DECL|method|parse ()
specifier|final
specifier|public
name|Template
name|parse
parameter_list|()
throws|throws
name|ParseException
block|{
name|Token
name|procudureName
decl_stmt|;
name|Template
name|template
init|=
operator|new
name|Template
argument_list|()
decl_stmt|;
name|Object
name|parameter
init|=
literal|null
decl_stmt|;
name|procudureName
operator|=
name|jj_consume_token
argument_list|(
name|IDENTIFIER
argument_list|)
expr_stmt|;
name|jj_consume_token
argument_list|(
literal|1
argument_list|)
expr_stmt|;
switch|switch
condition|(
operator|(
name|jj_ntk
operator|==
operator|-
literal|1
operator|)
condition|?
name|jj_ntk
argument_list|()
else|:
name|jj_ntk
condition|)
block|{
case|case
literal|5
case|:
case|case
name|IDENTIFIER
case|:
name|parameter
operator|=
name|Parameter
argument_list|()
expr_stmt|;
name|template
operator|.
name|addParameter
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
name|label_1
label|:
while|while
condition|(
literal|true
condition|)
block|{
switch|switch
condition|(
operator|(
name|jj_ntk
operator|==
operator|-
literal|1
operator|)
condition|?
name|jj_ntk
argument_list|()
else|:
name|jj_ntk
condition|)
block|{
case|case
literal|2
case|:
empty_stmt|;
break|break;
default|default:
name|jj_la1
index|[
literal|0
index|]
operator|=
name|jj_gen
expr_stmt|;
break|break
name|label_1
break|;
block|}
name|jj_consume_token
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|parameter
operator|=
name|Parameter
argument_list|()
expr_stmt|;
name|template
operator|.
name|addParameter
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|jj_la1
index|[
literal|1
index|]
operator|=
name|jj_gen
expr_stmt|;
empty_stmt|;
block|}
name|jj_consume_token
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|jj_consume_token
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|setProcedureName
argument_list|(
name|procudureName
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
name|template
return|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
DECL|method|Parameter ()
specifier|final
specifier|public
name|Object
name|Parameter
parameter_list|()
throws|throws
name|ParseException
block|{
name|Object
name|param
decl_stmt|;
switch|switch
condition|(
operator|(
name|jj_ntk
operator|==
operator|-
literal|1
operator|)
condition|?
name|jj_ntk
argument_list|()
else|:
name|jj_ntk
condition|)
block|{
case|case
name|IDENTIFIER
case|:
name|param
operator|=
name|InputParameter
argument_list|()
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
name|param
return|;
block|}
break|break;
case|case
literal|5
case|:
name|param
operator|=
name|OutParameter
argument_list|()
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
name|param
return|;
block|}
break|break;
default|default:
name|jj_la1
index|[
literal|2
index|]
operator|=
name|jj_gen
expr_stmt|;
name|jj_consume_token
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ParseException
argument_list|()
throw|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
DECL|method|InputParameter ()
specifier|final
specifier|public
name|InputParameter
name|InputParameter
parameter_list|()
throws|throws
name|ParseException
block|{
name|String
name|sqlTypeAsStr
decl_stmt|;
name|String
name|name
decl_stmt|;
name|String
name|valueSrcAsStr
decl_stmt|;
name|sqlTypeAsStr
operator|=
name|ParameterSqlType
argument_list|()
expr_stmt|;
name|jj_consume_token
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|valueSrcAsStr
operator|=
name|InputParameterSrc
argument_list|()
expr_stmt|;
name|int
name|sqlType
init|=
name|ParseHelper
operator|.
name|parseSqlType
argument_list|(
name|sqlTypeAsStr
argument_list|)
decl_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
operator|new
name|InputParameter
argument_list|(
name|createNextParameterName
argument_list|()
argument_list|,
name|sqlType
argument_list|,
name|valueSrcAsStr
argument_list|,
name|ParseHelper
operator|.
name|sqlTypeToJavaType
argument_list|(
name|sqlType
argument_list|,
name|sqlTypeAsStr
argument_list|)
argument_list|)
return|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
DECL|method|OutParameter ()
specifier|final
specifier|public
name|OutParameter
name|OutParameter
parameter_list|()
throws|throws
name|ParseException
block|{
name|String
name|sqlType
decl_stmt|;
name|String
name|name
decl_stmt|;
name|String
name|outHeader
decl_stmt|;
name|jj_consume_token
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|jj_consume_token
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|sqlType
operator|=
name|ParameterSqlType
argument_list|()
expr_stmt|;
name|jj_consume_token
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|outHeader
operator|=
name|OutHeader
argument_list|()
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
operator|new
name|OutParameter
argument_list|(
name|createNextParameterName
argument_list|()
argument_list|,
name|ParseHelper
operator|.
name|parseSqlType
argument_list|(
name|sqlType
argument_list|)
argument_list|,
name|outHeader
argument_list|)
return|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
DECL|method|ParameterSqlType ()
specifier|final
specifier|public
name|String
name|ParameterSqlType
parameter_list|()
throws|throws
name|ParseException
block|{
name|Token
name|t
decl_stmt|;
name|t
operator|=
name|jj_consume_token
argument_list|(
name|IDENTIFIER
argument_list|)
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
name|t
operator|.
name|toString
argument_list|()
return|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
DECL|method|OutHeader ()
specifier|final
specifier|public
name|String
name|OutHeader
parameter_list|()
throws|throws
name|ParseException
block|{
name|Token
name|token
decl_stmt|;
name|token
operator|=
name|jj_consume_token
argument_list|(
name|IDENTIFIER
argument_list|)
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
name|token
operator|.
name|toString
argument_list|()
return|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
DECL|method|InputParameterSrc ()
specifier|final
specifier|public
name|String
name|InputParameterSrc
parameter_list|()
throws|throws
name|ParseException
block|{
name|String
name|ret
decl_stmt|;
name|ret
operator|=
name|SimpleExpression
argument_list|()
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
name|ret
return|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
DECL|method|SimpleExpression ()
specifier|final
specifier|public
name|String
name|SimpleExpression
parameter_list|()
throws|throws
name|ParseException
block|{
name|Token
name|t
init|=
literal|null
decl_stmt|;
name|t
operator|=
name|jj_consume_token
argument_list|(
name|SIMPLE_EXP_TOKEN
argument_list|)
expr_stmt|;
block|{
if|if
condition|(
literal|true
condition|)
return|return
name|t
operator|.
name|toString
argument_list|()
return|;
block|}
throw|throw
operator|new
name|Error
argument_list|(
literal|"Missing return statement in function"
argument_list|)
throw|;
block|}
comment|/** Generated Token Manager. */
DECL|field|token_source
specifier|public
name|SSPTParserTokenManager
name|token_source
decl_stmt|;
DECL|field|jj_input_stream
name|SimpleCharStream
name|jj_input_stream
decl_stmt|;
comment|/** Current token. */
DECL|field|token
specifier|public
name|Token
name|token
decl_stmt|;
comment|/** Next token. */
DECL|field|jj_nt
specifier|public
name|Token
name|jj_nt
decl_stmt|;
DECL|field|jj_ntk
specifier|private
name|int
name|jj_ntk
decl_stmt|;
DECL|field|jj_gen
specifier|private
name|int
name|jj_gen
decl_stmt|;
DECL|field|jj_la1
specifier|final
specifier|private
name|int
index|[]
name|jj_la1
init|=
operator|new
name|int
index|[
literal|3
index|]
decl_stmt|;
DECL|field|jj_la1_0
specifier|static
specifier|private
name|int
index|[]
name|jj_la1_0
decl_stmt|;
static|static
block|{
name|jj_la1_init_0
argument_list|()
expr_stmt|;
block|}
DECL|method|jj_la1_init_0 ()
specifier|private
specifier|static
name|void
name|jj_la1_init_0
parameter_list|()
block|{
name|jj_la1_0
operator|=
operator|new
name|int
index|[]
block|{
literal|0x4
block|,
literal|0x220
block|,
literal|0x220
block|,}
expr_stmt|;
block|}
comment|/** Constructor with InputStream. */
DECL|method|SSPTParser (java.io.InputStream stream)
specifier|public
name|SSPTParser
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|stream
parameter_list|)
block|{
name|this
argument_list|(
name|stream
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor with InputStream and supplied encoding */
DECL|method|SSPTParser (java.io.InputStream stream, String encoding)
specifier|public
name|SSPTParser
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|stream
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
try|try
block|{
name|jj_input_stream
operator|=
operator|new
name|SimpleCharStream
argument_list|(
name|stream
argument_list|,
name|encoding
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|token_source
operator|=
operator|new
name|SSPTParserTokenManager
argument_list|(
name|jj_input_stream
argument_list|)
expr_stmt|;
name|token
operator|=
operator|new
name|Token
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
name|jj_gen
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
name|jj_la1
index|[
name|i
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream stream)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|stream
parameter_list|)
block|{
name|ReInit
argument_list|(
name|stream
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream stream, String encoding)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|stream
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
try|try
block|{
name|jj_input_stream
operator|.
name|ReInit
argument_list|(
name|stream
argument_list|,
name|encoding
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|token_source
operator|.
name|ReInit
argument_list|(
name|jj_input_stream
argument_list|)
expr_stmt|;
name|token
operator|=
operator|new
name|Token
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
name|jj_gen
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
name|jj_la1
index|[
name|i
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SSPTParser (java.io.Reader stream)
specifier|public
name|SSPTParser
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|stream
parameter_list|)
block|{
name|jj_input_stream
operator|=
operator|new
name|SimpleCharStream
argument_list|(
name|stream
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|token_source
operator|=
operator|new
name|SSPTParserTokenManager
argument_list|(
name|jj_input_stream
argument_list|)
expr_stmt|;
name|token
operator|=
operator|new
name|Token
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
name|jj_gen
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
name|jj_la1
index|[
name|i
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.Reader stream)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|stream
parameter_list|)
block|{
name|jj_input_stream
operator|.
name|ReInit
argument_list|(
name|stream
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|token_source
operator|.
name|ReInit
argument_list|(
name|jj_input_stream
argument_list|)
expr_stmt|;
name|token
operator|=
operator|new
name|Token
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
name|jj_gen
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
name|jj_la1
index|[
name|i
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
comment|/** Constructor with generated Token Manager. */
DECL|method|SSPTParser (SSPTParserTokenManager tm)
specifier|public
name|SSPTParser
parameter_list|(
name|SSPTParserTokenManager
name|tm
parameter_list|)
block|{
name|token_source
operator|=
name|tm
expr_stmt|;
name|token
operator|=
operator|new
name|Token
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
name|jj_gen
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
name|jj_la1
index|[
name|i
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (SSPTParserTokenManager tm)
specifier|public
name|void
name|ReInit
parameter_list|(
name|SSPTParserTokenManager
name|tm
parameter_list|)
block|{
name|token_source
operator|=
name|tm
expr_stmt|;
name|token
operator|=
operator|new
name|Token
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
name|jj_gen
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
name|jj_la1
index|[
name|i
index|]
operator|=
operator|-
literal|1
expr_stmt|;
block|}
DECL|method|jj_consume_token (int kind)
specifier|private
name|Token
name|jj_consume_token
parameter_list|(
name|int
name|kind
parameter_list|)
throws|throws
name|ParseException
block|{
name|Token
name|oldToken
decl_stmt|;
if|if
condition|(
operator|(
name|oldToken
operator|=
name|token
operator|)
operator|.
name|next
operator|!=
literal|null
condition|)
name|token
operator|=
name|token
operator|.
name|next
expr_stmt|;
else|else
name|token
operator|=
name|token
operator|.
name|next
operator|=
name|token_source
operator|.
name|getNextToken
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|kind
operator|==
name|kind
condition|)
block|{
name|jj_gen
operator|++
expr_stmt|;
return|return
name|token
return|;
block|}
name|token
operator|=
name|oldToken
expr_stmt|;
name|jj_kind
operator|=
name|kind
expr_stmt|;
throw|throw
name|generateParseException
argument_list|()
throw|;
block|}
comment|/** Get the next Token. */
DECL|method|getNextToken ()
specifier|final
specifier|public
name|Token
name|getNextToken
parameter_list|()
block|{
if|if
condition|(
name|token
operator|.
name|next
operator|!=
literal|null
condition|)
name|token
operator|=
name|token
operator|.
name|next
expr_stmt|;
else|else
name|token
operator|=
name|token
operator|.
name|next
operator|=
name|token_source
operator|.
name|getNextToken
argument_list|()
expr_stmt|;
name|jj_ntk
operator|=
operator|-
literal|1
expr_stmt|;
name|jj_gen
operator|++
expr_stmt|;
return|return
name|token
return|;
block|}
comment|/** Get the specific Token. */
DECL|method|getToken (int index)
specifier|final
specifier|public
name|Token
name|getToken
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Token
name|t
init|=
name|token
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|index
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|t
operator|.
name|next
operator|!=
literal|null
condition|)
name|t
operator|=
name|t
operator|.
name|next
expr_stmt|;
else|else
name|t
operator|=
name|t
operator|.
name|next
operator|=
name|token_source
operator|.
name|getNextToken
argument_list|()
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
DECL|method|jj_ntk ()
specifier|private
name|int
name|jj_ntk
parameter_list|()
block|{
if|if
condition|(
operator|(
name|jj_nt
operator|=
name|token
operator|.
name|next
operator|)
operator|==
literal|null
condition|)
return|return
operator|(
name|jj_ntk
operator|=
operator|(
name|token
operator|.
name|next
operator|=
name|token_source
operator|.
name|getNextToken
argument_list|()
operator|)
operator|.
name|kind
operator|)
return|;
else|else
return|return
operator|(
name|jj_ntk
operator|=
name|jj_nt
operator|.
name|kind
operator|)
return|;
block|}
DECL|field|jj_expentries
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|int
index|[]
argument_list|>
name|jj_expentries
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|int
index|[]
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|jj_expentry
specifier|private
name|int
index|[]
name|jj_expentry
decl_stmt|;
DECL|field|jj_kind
specifier|private
name|int
name|jj_kind
init|=
operator|-
literal|1
decl_stmt|;
comment|/** Generate ParseException. */
DECL|method|generateParseException ()
specifier|public
name|ParseException
name|generateParseException
parameter_list|()
block|{
name|jj_expentries
operator|.
name|clear
argument_list|()
expr_stmt|;
name|boolean
index|[]
name|la1tokens
init|=
operator|new
name|boolean
index|[
literal|10
index|]
decl_stmt|;
if|if
condition|(
name|jj_kind
operator|>=
literal|0
condition|)
block|{
name|la1tokens
index|[
name|jj_kind
index|]
operator|=
literal|true
expr_stmt|;
name|jj_kind
operator|=
operator|-
literal|1
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|jj_la1
index|[
name|i
index|]
operator|==
name|jj_gen
condition|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
literal|32
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
operator|(
name|jj_la1_0
index|[
name|i
index|]
operator|&
operator|(
literal|1
operator|<<
name|j
operator|)
operator|)
operator|!=
literal|0
condition|)
block|{
name|la1tokens
index|[
name|j
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|la1tokens
index|[
name|i
index|]
condition|)
block|{
name|jj_expentry
operator|=
operator|new
name|int
index|[
literal|1
index|]
expr_stmt|;
name|jj_expentry
index|[
literal|0
index|]
operator|=
name|i
expr_stmt|;
name|jj_expentries
operator|.
name|add
argument_list|(
name|jj_expentry
argument_list|)
expr_stmt|;
block|}
block|}
name|int
index|[]
index|[]
name|exptokseq
init|=
operator|new
name|int
index|[
name|jj_expentries
operator|.
name|size
argument_list|()
index|]
index|[]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|jj_expentries
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|exptokseq
index|[
name|i
index|]
operator|=
name|jj_expentries
operator|.
name|get
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ParseException
argument_list|(
name|token
argument_list|,
name|exptokseq
argument_list|,
name|tokenImage
argument_list|)
return|;
block|}
comment|/** Enable tracing. */
DECL|method|enable_tracing ()
specifier|final
specifier|public
name|void
name|enable_tracing
parameter_list|()
block|{   }
comment|/** Disable tracing. */
DECL|method|disable_tracing ()
specifier|final
specifier|public
name|void
name|disable_tracing
parameter_list|()
block|{   }
block|}
end_class

end_unit

